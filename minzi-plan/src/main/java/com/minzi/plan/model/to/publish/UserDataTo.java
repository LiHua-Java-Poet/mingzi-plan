package com.minzi.plan.model.to.publish;

import lombok.Data;

@Data
public class UserDataTo {

    /**
     * 任务总数
     */
    private Integer totalTask;

    /**
     * 进行中的任务数
     */
    private Integer processTask;

    /**
     * 完成的任务数
     */
    private Integer finishTask;

    /**
     * 取消的任务
     */
    private Integer cancelTask;

    /**
     * 计划总数
     */
    private Integer totalPlan;

    /**
     * 进行中的计划
     */
    private Integer processPlan;

    /**
     * 完成的计划
     */
    private Integer finishPlan;

    /**
     * 取消计划
     */
    private Integer cancelPlan;
}
