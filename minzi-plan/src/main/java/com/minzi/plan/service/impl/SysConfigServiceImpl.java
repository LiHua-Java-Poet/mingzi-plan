package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.dao.SysConfigDao;
import com.minzi.plan.model.entity.SysConfigEntity;
import com.minzi.plan.model.to.sysConfig.SysConfigInfoTo;
import com.minzi.plan.model.to.sysConfig.SysConfigListTo;
import com.minzi.plan.model.vo.sysConfig.SysConfigSaveVo;
import com.minzi.plan.model.vo.sysConfig.SysConfigUpdateVo;
import com.minzi.plan.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao,SysConfigEntity> implements SysConfigService {

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysConfigEntity> getListCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = new LambdaQueryWrapper<>();

        return wrapper;
    }

    @Override
    public List<SysConfigListTo> formatList(List<SysConfigEntity> list) {
        return list.stream().map(item -> {
            SysConfigListTo to = new SysConfigListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysConfigSaveVo sysConfigSaveVo) {
        SysConfigEntity entity = new SysConfigEntity();
        EntityUtils.copySameFields(sysConfigSaveVo, entity);
        sysConfigService.save(entity);
    }

    @Override
    public Wrapper<SysConfigEntity> getOneCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.last("limit 1");
        return wrapper;
    }

    @Override
    public SysConfigInfoTo formatOne(SysConfigEntity entity) {
        SysConfigInfoTo to = new SysConfigInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SysConfigUpdateVo UpdateVo) {
        SysConfigEntity entity = sysConfigService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        sysConfigService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        sysConfigService.remove(new LambdaQueryWrapper<SysConfigEntity>().in(SysConfigEntity::getId,ids));
    }
}