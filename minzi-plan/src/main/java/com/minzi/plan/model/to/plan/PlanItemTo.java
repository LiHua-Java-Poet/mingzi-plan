package com.minzi.plan.model.to.plan;


import lombok.Data;

@Data
public class PlanItemTo {

    /**
     * 序号
     */
    private Integer no;

    /**
     * 序号内容
     */
    private String itemContext;
}
