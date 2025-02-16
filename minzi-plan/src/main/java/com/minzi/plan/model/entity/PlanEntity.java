package com.minzi.plan.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("plan")
public class PlanEntity {

    public PlanEntity(Long id, String planName) {
        this.id = id;
        this.planName = planName;
    }

    public PlanEntity() {
    }

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 主键

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 计划进度
     */
    private Integer taskProgress;

    /**
     * 总任务数
     */
    private Integer taskTotal;

    /**
     * 周期类型 1 每天 2 隔天
     */
    private Integer cycleType;

    /**
     * 计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视
     */
    private Integer planType;

    /**
     * 计划信息
     */
    private String planInfo;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 预期完成时间
     */
    private Integer startTime;

    /**
     * 计划状态 1 进行中 2 已完成 3 已取消
     */
    private Integer status;

    /**
     * 任务规则
     */
    private String taskRule;

    /**
     * 预期完成时间
     */
    private Integer expectedTime;

    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deletedTime;
}
