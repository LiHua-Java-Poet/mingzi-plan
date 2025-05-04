package com.minzi.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.query.R;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.tools.lock.DistributedLock;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.SnowflakeIdGenerator;
import com.minzi.plan.common.UserContext;
import com.minzi.plan.dao.TaskDao;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskItemTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.model.vo.task.TaskUpdateVo;
import com.minzi.plan.service.PlanService;
import com.minzi.plan.service.TaskService;
import lombok.extern.java.Log;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Log
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {

    @Resource
    private TaskService taskService;

    @Resource
    private UserContext userContext;

    @Resource
    private PlanService planService;

    @Resource
    private EntityAct entityAct;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Wrapper<TaskEntity> getOneCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public TaskInfoTo formatOne(TaskEntity entity) {
        TaskInfoTo to = new TaskInfoTo();
        EntityUtils.copySameFields(entity, to);

        //补充计划的信息
        entityAct.oneToOne(entity, TaskEntity::getPlanEntity);
        Optional.ofNullable(entity.getPlanEntity()).ifPresent(value -> {
            PlanInfoTo planInfoTo = new PlanInfoTo();
            EntityUtils.copySameFields(value, planInfoTo);
            to.setPlanInfoTo(planInfoTo);
        });
        //将得到的任务备注格式化为对象
        List<TaskItemTo> taskItemTos = JSON.parseArray(entity.getRemark(), TaskItemTo.class);
        to.setItemToList(taskItemTos);
        return to;
    }

    @Override
    public Wrapper<TaskEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<TaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TaskEntity::getId);

        UserEntity userInfo = userContext.getUserInfo();
        if (userInfo != null) {
            wrapper.eq(TaskEntity::getUserId, userInfo.getId());
        }

        //查询 - Id
        Object id = lambdaHashMap.get(TaskEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), TaskEntity::getId, id);

        //查询 - 状态
        Object status = lambdaHashMap.get(TaskEntity::getStatus);
        wrapper.eq(!StringUtils.isEmpty(status), TaskEntity::getStatus, status);


        return wrapper;
    }

    @Override
    public List<TaskListTo> formatList(List<TaskEntity> list) {
        entityAct.oneToOne(list, TaskEntity::getPlanEntity);
        return list.stream().map(item -> {
            TaskListTo to = new TaskListTo();
            EntityUtils.copySameFields(item, to);
            List<TaskItemTo> taskItemTos = JSON.parseArray(item.getRemark(), TaskItemTo.class);
            to.setItemToList(taskItemTos);

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
        String uniqueCode = taskSaveVo.getUniqueCode();
        R.dataParamsAssert(uniqueCode == null, "校验码不能为空");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(uniqueCode);
        R.dataParamsAssert(value == null, "请不要重复提交");
        save.setRemark(JSON.toJSONString(taskSaveVo.getItemToList()));
        redisTemplate.delete(uniqueCode);
        taskService.save(save);
    }

    @Override
    public void update(TaskUpdateVo taskUpdateVo) {

    }

    @Override
    public void completeTask(String[] ids) {
        //完成任务
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getStatus, 1).in(TaskEntity::getId, ids));
        taskEntityList.forEach(item -> item.setStatus(2));

        //拿到对于的计划任务集合
        List<TaskEntity> planTaskEntityList = taskEntityList.stream().filter(b -> b.getPlanId() != 0L).collect(Collectors.toList());
        List<Long> planIdList = planTaskEntityList.stream().map(TaskEntity::getPlanId).collect(Collectors.toList());
        List<PlanEntity> planEntityList = planService.list(new LambdaQueryWrapper<PlanEntity>().in(PlanEntity::getId, planIdList));
        planEntityList.forEach(item -> {
            item.setTaskProgress(item.getTaskProgress() + 1);
            item.setStatus(item.getTaskTotal() <= item.getTaskProgress() ? 2 : 1);
        });
        if (!planEntityList.isEmpty()) {
            planService.updateBatchById(planEntityList);
        }
        taskService.updateBatchById(taskEntityList);
    }

    @Override
    public void cancelTask(String[] ids) {
        //完成任务
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().in(TaskEntity::getId, ids));
        taskEntityList.forEach(item -> item.setStatus(3));
        taskService.updateBatchById(taskEntityList);
    }

    @Override
    public String getUniqueCode() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String uniqueCode = SnowflakeIdGenerator.nextId() + "";
        valueOperations.set(uniqueCode, uniqueCode, 30);
        return uniqueCode;
    }

    @DistributedLock(prefixKey = "updateRemark")
    @Override
    public void updateRemark(TaskUpdateVo updateVo) {
        TaskEntity byId = taskService.getById(updateVo.getId());
        R.dataParamsAssert(byId == null, "请传入正确的任务ID");
        byId.setRemark(JSON.toJSONString(updateVo.getItemToList()));
        taskService.updateById(byId);
    }

    @Override
    public void delete(String[] ids) {
        taskService.removeByIds(Arrays.asList(ids));
    }
}
