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
import com.minzi.plan.dao.SysUserRoleDao;
import com.minzi.plan.model.entity.SysUserRoleEntity;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleInfoTo;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleListTo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleSaveVo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleUpdateVo;
import com.minzi.plan.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao,SysUserRoleEntity> implements SysUserRoleService{

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysUserRoleEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysUserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysUserRoleEntity::getId);
    
        Object id = lambdaHashMap.get(SysUserRoleEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysUserRoleEntity::getId, id);
    
        Object userId = lambdaHashMap.get(SysUserRoleEntity::getUserId);
        wrapper.eq(!StringUtils.isEmpty(userId), SysUserRoleEntity::getUserId, userId);
    
        Object roleId = lambdaHashMap.get(SysUserRoleEntity::getRoleId);
        wrapper.eq(!StringUtils.isEmpty(roleId), SysUserRoleEntity::getRoleId, roleId);
        return wrapper;
    }

    @Override
    public List<SysUserRoleListTo> formatList(List<SysUserRoleEntity> list) {
        entityAct.oneToOne(list,SysUserRoleEntity::getUserEntity);
        return list.stream().map(item -> {
            SysUserRoleListTo to = new SysUserRoleListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysUserRoleSaveVo sysUserRoleSaveVo) {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        EntityUtils.copySameFields(sysUserRoleSaveVo, entity);
        sysUserRoleService.save(entity);
    }

    @Override
    public SysUserRoleInfoTo formatOne(SysUserRoleEntity entity) {
        SysUserRoleInfoTo to = new SysUserRoleInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SysUserRoleUpdateVo UpdateVo) {
        SysUserRoleEntity entity = sysUserRoleService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        sysUserRoleService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
                        
        sysUserRoleService.update(
            new LambdaUpdateWrapper<SysUserRoleEntity>()
                    .set(SysUserRoleEntity::getDeleteTime, DateUtils.currentDateTime())
                    .in(SysUserRoleEntity::getId, ids)
    );
    }

}