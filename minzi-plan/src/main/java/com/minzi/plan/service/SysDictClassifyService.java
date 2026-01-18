package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SysDictClassifyEntity;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyInfoTo;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyListTo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifySaveVo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifyUpdateVo;


public interface SysDictClassifyService extends BaseService<SysDictClassifyEntity,SysDictClassifyListTo,SysDictClassifyInfoTo, SysDictClassifySaveVo, SysDictClassifyUpdateVo> {
}