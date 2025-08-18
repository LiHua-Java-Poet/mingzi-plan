package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minzi.common.core.tools.UserContext;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.to.publish.UserDataTo;
import com.minzi.plan.service.PlanService;
import com.minzi.plan.service.PublicService;
import com.minzi.plan.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PublicServiceImpl implements PublicService {

    @Resource
    private PlanService planService;

    @Resource
    private TaskService taskService;

    @Resource
    private UserContext userContext;

    @Override
    public UserDataTo getUserDataTo() {
        Long userId = userContext.getUserId();

        List<PlanEntity> planEntityList = planService.list(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getUserId, userId));
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getUserId, userId));
        UserDataTo to=new UserDataTo();
        to.setFinishPlan((int) planEntityList.stream().filter(b->b.getStatus()==2).count());
        to.setCancelPlan((int) planEntityList.stream().filter(b->b.getStatus()==3).count());
        to.setProcessPlan((int) planEntityList.stream().filter(b->b.getStatus()==1).count());
        to.setTotalPlan(planEntityList.size());

        to.setCancelTask((int) taskEntityList.stream().filter(b->b.getStatus()==3).count());
        to.setProcessTask((int) taskEntityList.stream().filter(b->b.getStatus()==1).count());
        to.setFinishTask((int) taskEntityList.stream().filter(b->b.getStatus()==2).count());
        to.setTotalTask(taskEntityList.size());
        return to;
    }
}
