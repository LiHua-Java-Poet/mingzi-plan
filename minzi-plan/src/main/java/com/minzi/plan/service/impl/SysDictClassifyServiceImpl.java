package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.SysDictClassifyDao;
import com.minzi.plan.model.entity.SysDictClassifyEntity;
import com.minzi.plan.model.entity.SysDictEntity;
import com.minzi.plan.model.entity.SysMenuEntity;
import com.minzi.plan.model.entity.SysRoleMenuEntity;
import com.minzi.plan.model.to.sysDict.SysDictListTo;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyInfoTo;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyListTo;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifySaveVo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifyUpdateVo;
import com.minzi.plan.service.SysDictClassifyService;
import com.minzi.plan.service.SysDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDictClassifyServiceImpl extends ServiceImpl<SysDictClassifyDao, SysDictClassifyEntity> implements SysDictClassifyService {

    @Resource
    private SysDictClassifyService sysDictClassifyService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysDictClassifyEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysDictClassifyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysDictClassifyEntity::getId);

        Object id = lambdaHashMap.get(SysDictClassifyEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysDictClassifyEntity::getId, id);

        Object parentId = lambdaHashMap.get(SysDictClassifyEntity::getParentId);
        wrapper.eq(!StringUtils.isEmpty(parentId), SysDictClassifyEntity::getParentId, parentId);

        Object classifyName = lambdaHashMap.get(SysDictClassifyEntity::getClassifyName);
        wrapper.like(!StringUtils.isEmpty(classifyName), SysDictClassifyEntity::getClassifyName, classifyName);

        Object sort = lambdaHashMap.get(SysDictClassifyEntity::getSort);
        wrapper.eq(!StringUtils.isEmpty(sort), SysDictClassifyEntity::getSort, sort);

        Object remark = lambdaHashMap.get(SysDictClassifyEntity::getRemark);
        wrapper.like(!StringUtils.isEmpty(remark), SysDictClassifyEntity::getRemark, remark);
        return wrapper;
    }

    @Override
    public List<SysDictClassifyListTo> formatList(List<SysDictClassifyEntity> list) {
        //先每个对象找一遍子节点
        List<SysDictClassifyListTo> listTos = list.stream().map(item -> {
            SysDictClassifyListTo to = new SysDictClassifyListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
        Map<Long, List<SysDictClassifyListTo>> parentIdMap = EntityUtils.resortEntityByColumnLevel2(listTos, SysDictClassifyListTo::getParentId);

        return listTos.stream().filter(b -> b.getParentId() == 0L).map(item -> {
            List<SysDictClassifyListTo> childrenList = parentIdMap.get(item.getId());
            item.setChildren(childrenList);
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysDictClassifySaveVo sysDictClassifySaveVo) {
        SysDictClassifyEntity entity = new SysDictClassifyEntity();
        EntityUtils.copySameFields(sysDictClassifySaveVo, entity);
        sysDictClassifyService.save(entity);
    }

    @Override
    public SysDictClassifyInfoTo formatOne(SysDictClassifyEntity entity) {
        SysDictClassifyInfoTo to = new SysDictClassifyInfoTo();
        EntityUtils.copySameFields(entity, to);

        List<SysDictEntity> dictEntityList = sysDictService.list(new LambdaQueryWrapper<SysDictEntity>().eq(SysDictEntity::getClassifyId, entity.getId()));
        List<SysDictListTo> sysDictListTos = sysDictService.formatList(dictEntityList);
        to.setSysDictListToList(sysDictListTos);

        return to;
    }

    @Override
    public void update(SysDictClassifyUpdateVo UpdateVo) {
        SysDictClassifyEntity entity = sysDictClassifyService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        entity.setUpdateTime(DateUtils.currentDateTime());
        sysDictClassifyService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {

        sysDictClassifyService.update(
                new LambdaUpdateWrapper<SysDictClassifyEntity>()
                        .set(SysDictClassifyEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(SysDictClassifyEntity::getId, ids)
        );
    }

}