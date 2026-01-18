package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysMenuEntity;
import com.minzi.plan.model.to.sysMenu.SysMenuInfoTo;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.vo.sysMenu.SysMenuSaveVo;
import com.minzi.plan.model.vo.sysMenu.SysMenuUpdateVo;


public interface SysMenuService extends BaseService<SysMenuEntity,SysMenuListTo,SysMenuInfoTo, SysMenuSaveVo, SysMenuUpdateVo> {
}