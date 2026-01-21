package com.minzi.plan.model.to.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskItemTo implements Serializable {

    /**
     * 序号
     */
    private Integer no;

    /**
     * 序号内容
     */
    private String itemContext;

    /**
     * 任务项标签
     */
    private String label;
}
