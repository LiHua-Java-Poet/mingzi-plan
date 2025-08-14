package com.minzi.plan.scheduled;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.service.PlanService;
import com.minzi.plan.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DailyPlan {

    @Resource
    private PlanService planService;

    @Resource
    private TaskService taskService;

    @Resource
    private EntityAct entityAct;

    /**
     * 扫描计划任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyTask() {
        // 这里是任务的具体逻辑
        //拿到所有进行中的计划
        List<PlanEntity> planEntityList = planService.list(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getStatus, 1));
        Map<Long, PlanEntity> planIdEntityMap = EntityUtils.resortEntityByColumnLevel1(planEntityList, PlanEntity::getId);
        entityAct.oneToMany(planEntityList,PlanEntity::getTaskEntityList);

        //拿到目前没有进行中的任务的计划
        List<Long> underPlanIdList = planEntityList.stream().map(PlanEntity::getId).collect(Collectors.toList());

        //拿到所有没有进行中的计划ID
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().select(TaskEntity::getPlanId).eq(TaskEntity::getStatus, 1).ne(TaskEntity::getPlanId, 0L));
        //这些是不需要生成任务的计划
        List<Long> planIdList = taskEntityList.stream().map(TaskEntity::getPlanId).collect(Collectors.toList());
        underPlanIdList.removeAll(planIdList);

        //剩下的都是要执行的
        underPlanIdList.forEach(item -> {
            PlanEntity planEntity = planIdEntityMap.get(item);

            List<TaskEntity> taskList = planEntity.getTaskEntityList();
            int size = (int) taskList.stream().filter(b -> b.getStatus() != 3).count();

            //按照这个计划生成任务
            TaskEntity task = new TaskEntity();
            task.setStatus(1);
            task.setTaskName(planEntity.getTaskRule() + " 第" + (size + 1 )+ "次");
            task.setTaskTime(DateUtils.currentDateTime());
            task.setUserId(planEntity.getUserId());
            task.setPlanId(planEntity.getId());
            task.setTaskType(planEntity.getPlanType());
            taskService.save(task);
        });
    }

}
