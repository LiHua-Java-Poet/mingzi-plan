package com.minzi.plan.model.vo.task;


import com.minzi.common.core.model.AnnexFile;
import com.minzi.plan.model.to.task.TaskItemTo;
import lombok.Data;

import java.util.List;

@Data
public class TaskUpdateVo {

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
     * 任务名
     */
    private String taskName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 任务时间
     */
    private Integer taskTime;

    /**
     * 任务状态 1 进行中 2 已完成 3 已取消
     */
    private Integer status;

    /**
     * 任务项的内容
     */
    private List<TaskItemTo> itemToList;

    /**
     * 计划用时
     */
    private Integer useTime;

    /**
     * 锁机用时
     */
    private Integer lockTime;

    /**
     * 计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视
     */
    private Integer planType;

    /**
     * 附件内容
     */
    private AnnexFile annexFile;

}
