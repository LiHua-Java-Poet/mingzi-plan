package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.query.R;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.common.UserContext;
import com.minzi.plan.dao.PlanDao;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.model.vo.plan.PlanSaveVo;
import com.minzi.plan.model.vo.plan.PlanUpdateVo;
import com.minzi.plan.service.PlanService;
import com.minzi.plan.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanDao, PlanEntity> implements PlanService {

    @Resource
    private PlanService planService;

    @Resource
    private UserContext userContext;

    @Resource
    private EntityAct entityAct;

    @Resource
    private TaskService taskService;

    @Override
    public Wrapper<PlanEntity> getListCondition(Map<String, Object> params) {
        LambdaQueryWrapper<PlanEntity> wrapper = new LambdaQueryWrapper<>();

        Object id = params.get("id");
        wrapper.eq(!StringUtils.isEmpty(id), PlanEntity::getId, id);

        Object status = params.get("status");
        wrapper.eq(!StringUtils.isEmpty(status), PlanEntity::getStatus, status);

        UserEntity userInfo = userContext.getUserInfo();
        if (userInfo != null) {
            wrapper.eq(PlanEntity::getUserId, userInfo.getId());
        }

        return wrapper;
    }

    @Override
    public List<PlanListTo> formatList(List<PlanEntity> list) {
        return list.stream().map(item -> {
            PlanListTo to = new PlanListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(PlanSaveVo planSaveVo) {
        PlanEntity entity = new PlanEntity();
        EntityUtils.copySameFields(planSaveVo, entity);

        //初始化计划信息
        UserEntity userInfo = userContext.getUserInfo();
        entity.setUserId(userInfo.getId());
        entity.setCycleType(1);
        entity.setStartTime(DateUtils.currentDateTime());
        entity.setStatus(1);
        entity.setPlanType(planSaveVo.getPlanType());
        entity.setTaskProgress(0);
        planService.save(entity);
    }

    @Override
    public Wrapper<PlanEntity> getOneCondition(Map<String, Object> params) {
        LambdaQueryWrapper<PlanEntity> wrapper = new LambdaQueryWrapper<>();

        Object id = params.get("id");
        wrapper.eq(!StringUtils.isEmpty(id), PlanEntity::getId, id);

        wrapper.last("limit 1");
        return wrapper;
    }

    @Override
    public PlanInfoTo formatOne(PlanEntity entity) {
        PlanInfoTo to = new PlanInfoTo();
        entityAct.oneToMany(entity, PlanEntity::getTaskEntityList);
        EntityUtils.copySameFields(entity, to);
        List<TaskEntity> taskEntityList = entity.getTaskEntityList();

        //拿到进行中的任务数
        to.setTowardProgress((int) taskEntityList.stream().filter(b -> b.getStatus() == 1).count());
        return to;
    }

    @Override
    public void update(PlanUpdateVo planUpdateVo) {
        PlanEntity entity = planService.getById(planUpdateVo.getId());
        EntityUtils.copySameFields(planUpdateVo, entity);
        planService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        planService.remove(new LambdaQueryWrapper<PlanEntity>().in(PlanEntity::getId,ids));
    }

    @Override
    public void deliver(Long id) {
        PlanEntity plan = planService.getById(id);
        R.dataParamsAssert(plan == null, "计划不存在，请检查上传的ID");
        entityAct.oneToMany(plan, PlanEntity::getTaskEntityList);

        List<TaskEntity> taskEntityList = plan.getTaskEntityList();
        int size = (int) taskEntityList.stream().filter(b -> b.getStatus() != 3).count();

        //按照这个计划生成任务
        TaskEntity task = new TaskEntity();
        task.setStatus(1);
        task.setTaskName(plan.getTaskRule() + " 第" + (size + 1) + "次");
        task.setTaskTime(DateUtils.currentDateTime());
        task.setUserId(plan.getUserId());
        task.setPlanId(plan.getId());
        taskService.save(task);
    }

    @Transactional
    @Override
    public void cancelPlan(String[] ids) {
        PlanEntity one = planService.getOne(new LambdaQueryWrapper<PlanEntity>().in(PlanEntity::getId, ids).last("limit 1"));
        R.dataParamsAssert(one == null, "请传入正确的计划Id");
        R.dataParamsAssert(one.getStatus() == 2, "已完成的任务不能取消");
        one.setStatus(3);
        entityAct.oneToMany(one,PlanEntity::getTaskEntityList);
        List<TaskEntity> taskEntityList = one.getTaskEntityList();
        List<TaskEntity> taskEntities = taskEntityList.stream().filter(b -> b.getStatus() == 1).peek(item -> item.setStatus(3)).collect(Collectors.toList());
        taskService.updateBatchById(taskEntities);
        planService.updateById(one);
    }
}
