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
import com.minzi.plan.dao.SysRoleDao;
import com.minzi.plan.model.entity.SysMenuEntity;
import com.minzi.plan.model.entity.SysRoleEntity;
import com.minzi.plan.model.entity.SysRoleMenuEntity;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.to.sysRole.SysRoleInfoTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuListTo;
import com.minzi.plan.model.vo.sysRole.SysRoleSaveVo;
import com.minzi.plan.model.vo.sysRole.SysRoleUpdateVo;
import com.minzi.plan.service.SysMenuService;
import com.minzi.plan.service.SysRoleMenuService;
import com.minzi.plan.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysRoleEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysRoleEntity::getId);

        Object id = lambdaHashMap.get(SysRoleEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysRoleEntity::getId, id);

        Object parentId = lambdaHashMap.get(SysRoleEntity::getParentId);
        wrapper.eq(!StringUtils.isEmpty(parentId), SysRoleEntity::getParentId, parentId);

        Object roleName = lambdaHashMap.get(SysRoleEntity::getRoleName);
        wrapper.like(!StringUtils.isEmpty(roleName), SysRoleEntity::getRoleName, roleName);
        return wrapper;
    }

    @Override
    public List<SysRoleListTo> formatList(List<SysRoleEntity> list) {
        //先每个对象找一遍子节点
        List<SysRoleListTo> listTos = list.stream().map(item -> {
            SysRoleListTo to = new SysRoleListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
        Map<Long, List<SysRoleListTo>> parentIdMap = EntityUtils.resortEntityByColumnLevel2(listTos, SysRoleListTo::getParentId);

        return listTos.stream().filter(b -> b.getParentId() == 0L).map(item -> {
            List<SysRoleListTo> childrenList = parentIdMap.get(item.getId());
            item.setChildren(childrenList);
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysRoleSaveVo sysRoleSaveVo) {
        SysRoleEntity entity = new SysRoleEntity();
        EntityUtils.copySameFields(sysRoleSaveVo, entity);
        sysRoleService.save(entity);
    }


    @Override
    public SysRoleInfoTo formatOne(SysRoleEntity entity) {
        SysRoleInfoTo to = new SysRoleInfoTo();
        EntityUtils.copySameFields(entity, to);

        //查一遍对应的菜单权限数据
        List<SysRoleMenuEntity> roleMenuEntityList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenuEntity>().eq(SysRoleMenuEntity::getRoleId,entity.getId()));
        entityAct.oneToOne(roleMenuEntityList,SysRoleMenuEntity::getSysMenuEntity);
        List<SysMenuEntity> menuEntityList = roleMenuEntityList.stream().map(SysRoleMenuEntity::getSysMenuEntity).collect(Collectors.toList());

        List<SysMenuListTo> sysMenuListTos = sysMenuService.formatList(menuEntityList);
        to.setSysMenuListTos(sysMenuListTos);
        return to;
    }

    @Override
    public void update(SysRoleUpdateVo UpdateVo) {
        SysRoleEntity entity = sysRoleService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        sysRoleService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {

        sysRoleService.update(
                new LambdaUpdateWrapper<SysRoleEntity>()
                        .set(SysRoleEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(SysRoleEntity::getId, ids)
        );
    }

}