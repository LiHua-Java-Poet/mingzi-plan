package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.query.R;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.core.tools.lock.DistributedLock;
import com.minzi.common.core.tools.resubmit.Resubmit;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.plan.dao.FileDao;
import com.minzi.plan.model.entity.FileEntity;
import com.minzi.plan.model.enums.FileEnums;
import com.minzi.plan.model.to.file.FileInfoTo;
import com.minzi.plan.model.to.file.FileListTo;
import com.minzi.plan.model.vo.file.FileSaveVo;
import com.minzi.plan.model.vo.file.FileUpdateVo;
import com.minzi.plan.service.FileService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {

    @Resource
    private FileService fileService;

    @Resource
    private EntityAct entityAct;

    @Resource
    private UserContext userContext;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Wrapper<FileEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<FileEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FileEntity::getId);
        wrapper.orderByAsc(FileEntity::getFileType);

        UserEntity userInfo = userContext.getUserInfo();
        wrapper.eq(userInfo != null, FileEntity::getUserId, userInfo.getId());

        Object pid = lambdaHashMap.get(FileEntity::getPid);
        wrapper.eq(!StringUtils.isEmpty(pid), FileEntity::getPid, pid);

        Object name = lambdaHashMap.get(FileEntity::getName);
        wrapper.like(!StringUtils.isEmpty(name), FileEntity::getName, name);

        Object fileType = lambdaHashMap.get(FileEntity::getFileType);
        wrapper.like(!StringUtils.isEmpty(fileType), FileEntity::getFileType, fileType);

        return wrapper;
    }

    @Override
    public List<FileListTo> formatList(List<FileEntity> list) {
        return list.stream()
                .map(item -> {
                    FileListTo to = new FileListTo();
                    EntityUtils.copySameFields(item, to);
                    return to;
                })
                .sorted(Comparator.comparingInt(to -> to.getFileType() == 2 ? 0 : 1)) // 2 在前
                .collect(Collectors.toList());
    }


    @DistributedLock(prefixKey = "file:", key = "#ids")
    @Resubmit(voClass = FileSaveVo.class)
    @Override
    public void add(FileSaveVo fileSaveVo) {
        UserEntity userInfo = userContext.getUserInfo();
        FileEntity entity = new FileEntity();
        EntityUtils.copySameFields(fileSaveVo, entity);
        entity.setUserId(userInfo.getId());
        fileService.save(entity);
    }

    @Override
    public FileInfoTo formatOne(FileEntity entity) {
        FileInfoTo to = new FileInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(FileUpdateVo UpdateVo) {
        FileEntity entity = fileService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        fileService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        // 1. 获取当前用户 ID
        Long userId = userContext.getUserId();

        // 2. 获取用户所有文件（用于构造 pid -> List<FileEntity> 映射）
        List<FileEntity> userFileList = fileService.list(
                new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getUserId, userId)
        );

        // 3. 构建 pid -> List<FileEntity> 的映射
        Map<Long, List<FileEntity>> pidListMap = EntityUtils.resortEntityByColumnLevel2(userFileList, FileEntity::getPid);

        // 4. 用于记录所有要删除的文件 ID
        Set<Long> deleteIdSet = new HashSet<>();

        // 5. 遍历每一个传入的文件/文件夹 ID，递归查找所有子文件 ID
        for (String idStr : ids) {
            Long id = Long.parseLong(idStr);
            collectAllChildIds(id, pidListMap, deleteIdSet);
        }

        // . 执行删除
        fileService.remove(new LambdaQueryWrapper<FileEntity>().in(FileEntity::getId, deleteIdSet));
    }

    /**
     * 递归收集所有子文件/子文件夹的 ID
     */
    private void collectAllChildIds(Long parentId, Map<Long, List<FileEntity>> pidMap, Set<Long> result) {
        result.add(parentId); // 加入当前 ID

        List<FileEntity> children = pidMap.get(parentId);
        if (children != null) {
            for (FileEntity child : children) {
                collectAllChildIds(child.getId(), pidMap, result); // 递归添加
            }
        }
    }

    @Override
    public void saveDocument(FileUpdateVo vo) {
        LambdaUpdateWrapper<FileEntity> wrapper = new LambdaUpdateWrapper<FileEntity>()
                .eq(FileEntity::getId, vo.getId())
                .set(FileEntity::getContent, vo.getContent());
        fileService.update(wrapper);
    }

    @Override
    public List<FileListTo> folderList(Map<String, Object> params) {
        Object id = params.get("id");
        Object pid = params.get("pid");
        R.dataParamsAssert(StringUtils.isEmpty(id), "ID不能为空");
        R.dataParamsAssert(StringUtils.isEmpty(pid), "PID不能为空");
        List<FileEntity> fileEntityList = fileService.list(new LambdaQueryWrapper<FileEntity>()
                .eq(FileEntity::getPid, pid)
                .eq(FileEntity::getFileType, FileEnums.MenuType.WEN_JIAN_JIA.getCode())
                .ne(FileEntity::getId, id));
        return fileEntityList.stream().map(item -> {
            FileListTo to = new FileListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }
}