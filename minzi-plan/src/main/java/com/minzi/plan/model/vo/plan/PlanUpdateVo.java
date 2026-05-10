package com.minzi.plan.model.vo.plan;


import com.minzi.common.core.model.AnnexFile;
import com.minzi.plan.model.to.plan.PlanItemTo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class PlanUpdateVo {

    /**
     * id
     */
    private Long id;

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
     * 计划内容列表
     */
    @ApiModelProperty(value = "计划内容列表")
    private List<PlanItemTo> itemToList;

    /**
     * 计划图标
     */
    private String icon;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 任务数
     */
    private Integer taskTotal;

    /**
     * 任务规则
     */
    private String taskRule;

    /**
     * 附件内容
     */
    private List<AnnexFile> annexFiles;

}
