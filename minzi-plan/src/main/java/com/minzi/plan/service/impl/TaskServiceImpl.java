package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.common.UserContext;
import com.minzi.plan.dao.TaskDao;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.model.vo.task.TaskUpdateVo;
import com.minzi.plan.service.TaskService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {

    @Resource
    private TaskService taskService;

    @Resource
    private UserContext userContext;

    @Override
    public Wrapper<TaskEntity> getOneCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public TaskInfoTo formatOne(TaskEntity entity) {
        TaskInfoTo to = new TaskInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public Wrapper<TaskEntity> getListCondition(Map<String, Object> params) {
        LambdaQueryWrapper<TaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TaskEntity::getId);

        UserEntity userInfo = userContext.getUserInfo();
        if (userInfo != null) {
            wrapper.eq(TaskEntity::getUserId, userInfo.getId());
        }

        Object status = params.get("status");
        wrapper.eq(!StringUtils.isEmpty(status), TaskEntity::getStatus, status);


        return wrapper;
    }

    @Override
    public List<TaskListTo> formatList(List<TaskEntity> list) {
        return list.stream().map(item -> {
            TaskListTo to = new TaskListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(TaskSaveVo taskSaveVo) {
        TaskEntity save = new TaskEntity();
        EntityUtils.copySameFields(taskSaveVo, save);

        UserEntity userInfo = userContext.getUserInfo();
        save.setUserId(userInfo.getId());
        save.setStatus(1);
        taskService.save(save);
    }

    @Override
    public void update(TaskUpdateVo taskUpdateVo) {

    }

    @Override
    public void completeTask(String[] ids) {
        //完成任务
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().in(TaskEntity::getId, ids));
        taskEntityList.forEach(item->{
            item.setStatus(2);
        });
        taskService.updateBatchById(taskEntityList);
    }

    @Override
    public void cancelTask(String[] ids) {
        //完成任务
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().in(TaskEntity::getId, ids));
        taskEntityList.forEach(item->{
            item.setStatus(3);
        });
        taskService.updateBatchById(taskEntityList);
    }
}
