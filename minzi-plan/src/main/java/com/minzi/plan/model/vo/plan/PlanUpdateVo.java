package com.minzi.plan.model.vo.plan;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PlanUpdateVo {


    /**
     *  计划名
     */
    @ApiModelProperty(value = "计划名")
    private String planName;

    /**
     *  周期类型 1 每天 2 隔天
     */
    @ApiModelProperty(value = "周期类型 1 每天 2 隔天")
    private Integer cycleType;

    /**
     *  计划类型 1 预设 2 循环
     */
    @ApiModelProperty(value = "计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视 ")
    private Integer planType;

    /**
     * 计划信息
     */
    @ApiModelProperty(value = "计划信息")
    private String planInfo;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

}
