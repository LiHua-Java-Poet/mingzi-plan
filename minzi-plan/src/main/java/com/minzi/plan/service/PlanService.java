package com.minzi.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.to.plan.PlanListTo;

import java.util.List;

public interface PlanService extends IService<PlanEntity> {

    List<PlanListTo> getList();
}
