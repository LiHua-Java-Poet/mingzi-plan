package com.minzi.plan.model.vo.task;

import com.minzi.common.core.model.AnnexFile;
import com.minzi.plan.model.to.task.TaskItemTo;
import lombok.Data;

import java.util.List;

@Data
public class TaskSaveVo {

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 任务名
     */
    private String remark;

    /**
     * 任务项的内容
     */
    private List<TaskItemTo> itemToList;

    /**
     * 任务时间
     */
    private Integer taskTime;

    /**
     * 计划用时
     */
    private Integer useTime;

    /**
     * 锁机用时
     */
    private Integer lockTime;

    /**
     * 唯一码
     */
    private String uniqueCode;

    /**
     * 计划类型 1 学习 2 锻炼 3 写作 4 阅读 5 影视
     */
    private Integer planType;

    /**
     * 附件内容
     */
    private AnnexFile annexFile;
}
