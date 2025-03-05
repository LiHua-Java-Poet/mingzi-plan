package com.minzi.plan.model.vo.task;

import lombok.Data;

@Data
public class TaskSaveVo {

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 任务时间
     */
    private Integer taskTime;

    /**
     * 计划用时
     */
    private Integer useTime;

    /**
     * 锁机用时
     */
    private Integer lockTime;

    /**
     * 唯一码
     */
    private String uniqueCode;

}
