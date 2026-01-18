package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysRoleMenuEntity;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuInfoTo;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuListTo;
import com.minzi.plan.model.vo.sysRoleMenu.PermissionVo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuSaveVo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuUpdateVo;

import java.util.List;


public interface SysRoleMenuService extends BaseService<SysRoleMenuEntity,SysRoleMenuListTo,SysRoleMenuInfoTo, SysRoleMenuSaveVo, SysRoleMenuUpdateVo> {

    /**
     * 删除对应的菜单用户
     * @param vo
     */
    void deletePermission(PermissionVo vo);

    /**
     * 新增对应的角色菜单
     * @param vo
     */
    void addList(List<SysRoleMenuSaveVo> vo);
}