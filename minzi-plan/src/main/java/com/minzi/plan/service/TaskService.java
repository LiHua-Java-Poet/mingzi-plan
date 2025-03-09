package com.minzi.plan.service;

import com.minzi.common.service.BaseService;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.model.vo.task.TaskUpdateVo;

public interface TaskService extends BaseService<TaskEntity, TaskListTo, TaskInfoTo, TaskSaveVo, TaskUpdateVo> {
    /**
     * 完成任务
     * @param ids 完成的任务ID
     */
    void completeTask(String[] ids);


    /**
     * 取消任务
     * @param ids 任务ID
     */
    void cancelTask(String[] ids);

    /***
     * 获取到唯一码
     * @return 返回的唯一码
     */
    String getUniqueCode();

    /**
     * 更新用户的备注
     * @param updateVo 更新备注
     */
    void updateRemark(TaskUpdateVo updateVo);
}
