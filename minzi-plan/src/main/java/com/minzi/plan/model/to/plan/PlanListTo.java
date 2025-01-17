package com.minzi.plan.model.to.plan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "计划列表返回实体类")
@Data
public class PlanListTo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计划主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  用户ID
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     *  计划名
     */
    private String planName;

    /**
     *  计划进度
     */
    private Integer taskProgress;

    /**
     *  总任务数
     */
    private Integer taskTotal;

    /**
     *  周期类型 1 每天 2 隔天
     */
    private Integer cycleType;

    /**
     *  计划类型 1 预设 2 循环
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
     * 删除时间
     */
    private Integer deletedTime;
}
