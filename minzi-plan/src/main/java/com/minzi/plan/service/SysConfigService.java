package com.minzi.plan.service;

import com.minzi.common.service.BaseService;
import com.minzi.plan.model.entity.SysConfigEntity;
import com.minzi.plan.model.to.sysConfig.SysConfigInfoTo;
import com.minzi.plan.model.to.sysConfig.SysConfigListTo;
import com.minzi.plan.model.vo.sysConfig.SysConfigSaveVo;
import com.minzi.plan.model.vo.sysConfig.SysConfigUpdateVo;


public interface SysConfigService extends BaseService<SysConfigEntity,SysConfigListTo,SysConfigInfoTo, SysConfigSaveVo, SysConfigUpdateVo> {
}