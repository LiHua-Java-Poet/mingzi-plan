package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.SysUserRoleDao;
import com.minzi.plan.model.entity.SysRoleEntity;
import com.minzi.plan.model.entity.SysRoleMenuEntity;
import com.minzi.plan.model.entity.SysUserRoleEntity;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleInfoTo;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleListTo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuSaveVo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleSaveVo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleUpdateVo;
import com.minzi.plan.service.SysUserRoleService;
import com.minzi.plan.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao,SysUserRoleEntity> implements SysUserRoleService{

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private EntityAct entityAct;

    @Resource
    private UserService userService;

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

    @Transactional
    @Override
    public void userRelateRole(List<SysUserRoleSaveVo> vos) {
        //关联用户角色表
        if (vos.isEmpty()) return;
        SysUserRoleSaveVo sysUserRoleSaveVo = vos.get(0);
        Long userId = sysUserRoleSaveVo.getUserId();

        //现存的用户角色表
        List<SysUserRoleEntity> dbUserRoleList = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, userId));
        Set<Long> dbRoleIdSet = dbUserRoleList.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toSet());
        Set<Long> saveRoleIdSet = vos.stream().map(SysUserRoleSaveVo::getRoleId).collect(Collectors.toSet());
        Set<Long> removeRoleIdSet = vos.stream().map(SysUserRoleSaveVo::getRoleId).collect(Collectors.toSet());

        saveRoleIdSet.removeAll(dbRoleIdSet);
        dbRoleIdSet.removeAll(removeRoleIdSet);

        List<SysUserRoleEntity> saveList = saveRoleIdSet.stream().map(item -> {
            SysUserRoleEntity vo = new SysUserRoleEntity();
            vo.setUserId(userId);
            vo.setRoleId(item);
            return vo;
        }).collect(Collectors.toList());

        sysUserRoleService.saveBatch(saveList);
        if (dbRoleIdSet.isEmpty()) return;
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, userId).in(SysUserRoleEntity::getRoleId, dbRoleIdSet));
        userService.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getUpdateTime,DateUtils.currentDateTime()).eq(UserEntity::getId,userId));
    }
}