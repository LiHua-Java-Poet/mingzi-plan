package com.minzi.plan.service;

import com.minzi.common.service.BaseService;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.model.vo.task.TaskUpdateVo;

public interface TaskService extends BaseService<TaskEntity, TaskListTo, TaskInfoTo, TaskSaveVo, TaskUpdateVo> {
}
