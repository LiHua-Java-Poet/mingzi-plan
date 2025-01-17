package com.minzi.plan.model.vo.plan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PlanSaveVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计划主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  计划名
     */
    @ApiModelProperty(value = "计划名")
    private String planName;

    /**
     *  计划进度
     */
    @ApiModelProperty(value = "计划进度")
    private Integer taskProgress;

    /**
     *  总任务数
     */
    @ApiModelProperty(value = "总任务数")
    private Integer taskTotal;

    /**
     *  周期类型 1 每天 2 隔天
     */
    @ApiModelProperty(value = "周期类型 1 每天 2 隔天")
    private Integer cycleType;

    /**
     *  计划类型 1 预设 2 循环
     */
    @ApiModelProperty(value = "计划类型 1 预设 2 循环")
    private Integer planType;

    /**
     * 计划信息
     */
    @ApiModelProperty(value = "计划信息")
    private String planInfo;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

}
