package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysUserRoleEntity;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleInfoTo;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleListTo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleSaveVo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleUpdateVo;


public interface SysUserRoleService extends BaseService<SysUserRoleEntity,SysUserRoleListTo,SysUserRoleInfoTo, SysUserRoleSaveVo, SysUserRoleUpdateVo> {
}