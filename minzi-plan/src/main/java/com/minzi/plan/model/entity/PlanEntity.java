package com.minzi.plan.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("plan")
public class PlanEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 主键

    private Long userId; // 用户ID

    private String planName; // 计划名

    private Integer taskProgress; // 计划进度

    private Integer taskTotal; // 总任务数

    private Integer cycleType; // 周期类型 1 每天 2 隔天

    private Integer planType; // 计划类型 1 预设 2 循环

    private String planInfo; // 计划信息

    private String icon; // 图标

    private String description; // 描述

    /**
     * 预期完成时间
     */
    private Integer expectedTime;

    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deletedTime;
}
