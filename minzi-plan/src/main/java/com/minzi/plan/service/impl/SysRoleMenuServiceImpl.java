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
import com.minzi.plan.dao.SysRoleMenuDao;
import com.minzi.plan.model.entity.SysRoleMenuEntity;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuInfoTo;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuListTo;
import com.minzi.plan.model.vo.sysRoleMenu.PermissionVo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuSaveVo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuUpdateVo;
import com.minzi.plan.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysRoleMenuEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysRoleMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysRoleMenuEntity::getId);

        Object id = lambdaHashMap.get(SysRoleMenuEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysRoleMenuEntity::getId, id);

        Object roleId = lambdaHashMap.get(SysRoleMenuEntity::getRoleId);
        wrapper.eq(!StringUtils.isEmpty(roleId), SysRoleMenuEntity::getRoleId, roleId);

        Object menuId = lambdaHashMap.get(SysRoleMenuEntity::getMenuId);
        wrapper.eq(!StringUtils.isEmpty(menuId), SysRoleMenuEntity::getMenuId, menuId);
        return wrapper;
    }

    @Override
    public List<SysRoleMenuListTo> formatList(List<SysRoleMenuEntity> list) {
        return list.stream().map(item -> {
            SysRoleMenuListTo to = new SysRoleMenuListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysRoleMenuSaveVo sysRoleMenuSaveVo) {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        EntityUtils.copySameFields(sysRoleMenuSaveVo, entity);
        sysRoleMenuService.save(entity);
    }

    @Override
    public SysRoleMenuInfoTo formatOne(SysRoleMenuEntity entity) {
        SysRoleMenuInfoTo to = new SysRoleMenuInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SysRoleMenuUpdateVo UpdateVo) {
        SysRoleMenuEntity entity = sysRoleMenuService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        sysRoleMenuService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        sysRoleMenuService.update(
                new LambdaUpdateWrapper<SysRoleMenuEntity>()
                        .set(SysRoleMenuEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(SysRoleMenuEntity::getId, ids)
        );
    }

    @Override
    public void deletePermission(PermissionVo vo) {
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, vo.getRoleId()).eq(SysRoleMenuEntity::getMenuId, vo.getMenuId()));
    }

    @Override
    public void addList(List<SysRoleMenuSaveVo> vos) {
        if (vos.isEmpty()) return;
        SysRoleMenuSaveVo sysRoleMenuSaveVo = vos.get(0);
        Long roleId = sysRoleMenuSaveVo.getRoleId();

        //现存的角色菜单表
        List<SysRoleMenuEntity> dbRoleMenuList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, roleId));
        Set<Long> dbRoleMenuIdSet = dbRoleMenuList.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toSet());
        Set<Long> saveRoleMenuIdSet = vos.stream().map(SysRoleMenuSaveVo::getMenuId).collect(Collectors.toSet());
        Set<Long> removeRoleMenuIdSet = vos.stream().map(SysRoleMenuSaveVo::getMenuId).collect(Collectors.toSet());

        //得到要保存的菜单id
        saveRoleMenuIdSet.removeAll(dbRoleMenuIdSet);
        dbRoleMenuIdSet.removeAll(removeRoleMenuIdSet);

        List<SysRoleMenuEntity> saveList = saveRoleMenuIdSet.stream().map(item -> {
            SysRoleMenuEntity vo = new SysRoleMenuEntity();
            vo.setRoleId(roleId);
            vo.setMenuId(item);
            return vo;
        }).collect(Collectors.toList());

        sysRoleMenuService.saveBatch(saveList);
        if (dbRoleMenuIdSet.isEmpty()) return;
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId, roleId).in(SysRoleMenuEntity::getMenuId, dbRoleMenuIdSet));
    }
}