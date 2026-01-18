package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysDictEntity;
import com.minzi.plan.model.to.sysDict.SysDictInfoTo;
import com.minzi.plan.model.to.sysDict.SysDictListTo;
import com.minzi.plan.model.vo.sysDict.SysDictSaveVo;
import com.minzi.plan.model.vo.sysDict.SysDictUpdateVo;


public interface SysDictService extends BaseService<SysDictEntity,SysDictListTo,SysDictInfoTo, SysDictSaveVo, SysDictUpdateVo> {
}