package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysRoleEntity;
import com.minzi.plan.model.to.sysRole.SysRoleInfoTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import com.minzi.plan.model.vo.sysRole.SysRoleSaveVo;
import com.minzi.plan.model.vo.sysRole.SysRoleUpdateVo;


public interface SysRoleService extends BaseService<SysRoleEntity,SysRoleListTo,SysRoleInfoTo, SysRoleSaveVo, SysRoleUpdateVo> {
}