package com.minzi.plan.model.to.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TaskInfoTo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 计划信息
     */
    private PlanInfoTo planInfoTo;

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
     * 删除时间
     */
    private Integer deleteTime;
}
