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
import com.minzi.plan.dao.SysDictDao;
import com.minzi.plan.model.entity.SysDictClassifyEntity;
import com.minzi.plan.model.entity.SysDictEntity;
import com.minzi.plan.model.to.sysDict.SysDictInfoTo;
import com.minzi.plan.model.to.sysDict.SysDictListTo;
import com.minzi.plan.model.vo.sysDict.SysDictSaveVo;
import com.minzi.plan.model.vo.sysDict.SysDictUpdateVo;
import com.minzi.plan.service.SysDictClassifyService;
import com.minzi.plan.service.SysDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysDictClassifyService sysDictClassifyService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysDictEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysDictEntity> wrapper = new LambdaQueryWrapper<>();
//        wrapper.orderByDesc(SysDictEntity::getId);
        wrapper.orderByDesc(SysDictEntity::getSort);

        Object id = lambdaHashMap.get(SysDictEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysDictEntity::getId, id);

        Object classifyId = lambdaHashMap.get(SysDictEntity::getClassifyId);
        wrapper.eq(!StringUtils.isEmpty(classifyId), SysDictEntity::getClassifyId, classifyId);

        Object dictName = lambdaHashMap.get(SysDictEntity::getDictName);
        wrapper.like(!StringUtils.isEmpty(dictName), SysDictEntity::getDictName, dictName);

        Object dictCode = lambdaHashMap.get(SysDictEntity::getDictCode);
        wrapper.like(!StringUtils.isEmpty(dictCode), SysDictEntity::getDictCode, dictCode);

        Object sort = lambdaHashMap.get(SysDictEntity::getSort);
        wrapper.eq(!StringUtils.isEmpty(sort), SysDictEntity::getSort, sort);

        Object remark = lambdaHashMap.get(SysDictEntity::getRemark);
        wrapper.like(!StringUtils.isEmpty(remark), SysDictEntity::getRemark, remark);

        Object classifyCode = params.get("classifyCode");
        SysDictClassifyEntity classify = sysDictClassifyService.getOne(new LambdaQueryWrapper<SysDictClassifyEntity>().eq(SysDictClassifyEntity::getClassifyCode, classifyCode));
        if (classify != null) wrapper.like(!StringUtils.isEmpty(classify), SysDictEntity::getClassifyId, classify.getId());


        return wrapper;
    }

    @Override
    public List<SysDictListTo> formatList(List<SysDictEntity> list) {
        return list.stream().map(item -> {
            SysDictListTo to = new SysDictListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysDictSaveVo sysDictSaveVo) {
        SysDictEntity entity = new SysDictEntity();
        EntityUtils.copySameFields(sysDictSaveVo, entity);
        sysDictService.save(entity);
    }

    @Override
    public SysDictInfoTo formatOne(SysDictEntity entity) {
        SysDictInfoTo to = new SysDictInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SysDictUpdateVo UpdateVo) {
        SysDictEntity entity = sysDictService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        entity.setUpdateTime(DateUtils.currentDateTime());
        sysDictService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {

        sysDictService.update(
                new LambdaUpdateWrapper<SysDictEntity>()
                        .set(SysDictEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(SysDictEntity::getId, ids)
        );
    }

}