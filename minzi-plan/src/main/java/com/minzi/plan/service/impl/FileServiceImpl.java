package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.query.R;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.tools.lock.DistributedLock;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.plan.common.UserContext;
import com.minzi.plan.dao.FileDao;
import com.minzi.plan.model.entity.FileEntity;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.to.file.FileInfoTo;
import com.minzi.plan.model.to.file.FileListTo;
import com.minzi.plan.model.vo.file.FileSaveVo;
import com.minzi.plan.model.vo.file.FileUpdateVo;
import com.minzi.plan.service.FileService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
        wrapper.eq(StringUtils.isEmpty(pid), FileEntity::getPid, pid);

        return wrapper;
    }

    @Override
    public List<FileListTo> formatList(List<FileEntity> list) {
        return list.stream().map(item -> {
            FileListTo to = new FileListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @DistributedLock(prefixKey = "file:", key = "#ids")
    @Override
    public void add(FileSaveVo fileSaveVo) {
        String uniqueCode = fileSaveVo.getUniqueCode();
        R.dataParamsAssert(uniqueCode == null, "校验码不能为空");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(uniqueCode);
        redisTemplate.delete(uniqueCode);
        R.dataParamsAssert(value == null, "请不要重复提交");
        UserEntity userInfo = userContext.getUserInfo();
        FileEntity entity = new FileEntity();
        EntityUtils.copySameFields(fileSaveVo, entity);
        entity.setUserId(userInfo.getId());
        fileService.save(entity);
    }

    @Override
    public Wrapper<FileEntity> getOneCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<FileEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.last("limit 1");
        return wrapper;
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
        fileService.remove(new LambdaQueryWrapper<FileEntity>().in(FileEntity::getId, ids));
    }
}