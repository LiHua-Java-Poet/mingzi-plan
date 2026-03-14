package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.TaskLogEntity;
import com.minzi.plan.model.to.taskLog.TaskLogInfoTo;
import com.minzi.plan.model.to.taskLog.TaskLogListTo;
import com.minzi.plan.model.vo.taskLog.TaskLogSaveVo;
import com.minzi.plan.model.vo.taskLog.TaskLogUpdateVo;


public interface TaskLogService extends BaseService<TaskLogEntity,TaskLogListTo,TaskLogInfoTo, TaskLogSaveVo, TaskLogUpdateVo> {
}