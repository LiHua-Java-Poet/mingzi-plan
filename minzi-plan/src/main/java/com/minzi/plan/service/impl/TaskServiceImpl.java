package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.plan.dao.TaskDao;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.service.TaskService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Log
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {


    @Override
    public Wrapper<TaskEntity> getOneCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public TaskInfoTo formatOne(TaskEntity entity) {
        return null;
    }

    @Override
    public Wrapper<TaskEntity> getListCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<TaskListTo> formatList(List<TaskEntity> list) {
        return null;
    }

    @Override
    public void add(TaskSaveVo taskSaveVo) {

    }
}
