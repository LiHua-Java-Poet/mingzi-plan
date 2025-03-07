package com.minzi.plan.service;

import com.minzi.common.service.BaseService;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.model.vo.plan.PlanSaveVo;
import com.minzi.plan.model.vo.plan.PlanUpdateVo;


public interface PlanService extends BaseService<PlanEntity,PlanListTo,PlanInfoTo, PlanSaveVo, PlanUpdateVo> {

    /**
     * 下发任务
     * @param id 计划ID
     */
    void deliver(Long id);

    /**
     * 取消计划
     * @param ids 计划的id集合
     */
    void cancelPlan(String[] ids);
}
