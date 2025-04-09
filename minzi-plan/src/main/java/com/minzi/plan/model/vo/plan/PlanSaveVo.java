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
     *  计划名
     */
    @ApiModelProperty(value = "计划名")
    private String planName;

    /**
     *  计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视
     */
    @ApiModelProperty(value = "计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视 ")
    private Integer planType;

    /**
     *  总任务数
     */
    @ApiModelProperty(value = "总任务数")
    private Integer taskTotal;

    /**
     * 计划图标
     */
    @ApiModelProperty(value = "计划图标")
    private String icon;

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

    /**
     * 计划信息
     */
    @ApiModelProperty(value = "计划信息")
    private String taskRule;

}
