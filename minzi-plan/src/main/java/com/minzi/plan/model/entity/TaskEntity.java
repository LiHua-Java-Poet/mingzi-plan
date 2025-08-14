package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.common.annotation.OneToOne;
import com.minzi.plan.service.PlanService;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("task")
public class TaskEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 所属计划ID
     */
    private Long planId;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 任务时间
     */
    private Integer taskTime;

    /**
     * 任务状态 1 进行中 2 已完成 3 已取消
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 计划用时
     */
    private Integer useTime;

    /**
     * 锁机用时
     */
    private Integer lockTime;

    /**
     * 附件内容
     */
    private String annexFile;

    /**
     * 任务类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视
     */
    private Integer taskType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private Integer createTime;

    /**
     * 删除时间
     */
    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deleteTime;

    @OneToOne(localKey = "plan_id", foreignKey = "id", targetService = PlanService.class)
    @TableField(exist = false)
    private PlanEntity planEntity;

}