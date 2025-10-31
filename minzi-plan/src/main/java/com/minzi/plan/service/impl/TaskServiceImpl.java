package com.minzi.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.AnnexFile;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.model.enums.AnnexFileEnum;
import com.minzi.common.core.query.R;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.core.tools.lock.DistributedLock;
import com.minzi.common.core.tools.resubmit.Resubmit;
import com.minzi.common.core.tools.utils.AnnexFileUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.SnowflakeIdGenerator;
import com.minzi.plan.dao.TaskDao;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.entity.TaskEntity;
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
import java.util.*;
import java.util.function.Function;
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

        //将得到的任务附件格式化为对象
        List<AnnexFile> annexFiles = JSON.parseArray(entity.getAnnexFile(), AnnexFile.class);
        to.setAnnexFiles(annexFiles);
        return to;
    }

    @Override
    public Wrapper<TaskEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<TaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TaskEntity::getId);

        UserEntity userInfo = userContext.getUserInfo();
//        if (userInfo != null) {
//            wrapper.eq(TaskEntity::getUserId, userInfo.getId());
//        }
        wrapper.eq(userInfo != null,TaskEntity::getUserId, userInfo.getId());

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

    @Resubmit(voClass = TaskSaveVo.class)
    @Override
    public void add(TaskSaveVo taskSaveVo) {
        TaskEntity save = new TaskEntity();
        EntityUtils.copySameFields(taskSaveVo, save);

        UserEntity userInfo = userContext.getUserInfo();
        save.setUserId(userInfo.getId());
        save.setStatus(1);
        save.setRemark(JSON.toJSONString(taskSaveVo.getItemToList()));

        //保存附件,先处理一下附件的格式
        AnnexFileUtils.fillAnnexFiles(taskSaveVo,save);
        taskService.save(save);
    }

    @Resubmit(voClass = TaskUpdateVo.class)
    @Override
    public void update(TaskUpdateVo taskUpdateVo) {
        //更新任务的内容
        TaskEntity update = new TaskEntity();
        EntityUtils.copySameFields(taskUpdateVo, update);

        UserEntity userInfo = userContext.getUserInfo();
        update.setUserId(userInfo.getId());
        update.setRemark(JSON.toJSONString(taskUpdateVo.getItemToList()));

        //保存附件,先处理一下附件的格式
        List<AnnexFile> annexFiles = taskUpdateVo.getAnnexFiles();
        Map<String, Integer> annexFileEnumMap = AnnexFileEnum.FileType.toMap(AnnexFileEnum.FileType::getName, AnnexFileEnum.FileType::getCode);
        Optional.ofNullable(annexFiles).ifPresent(value->{
            value.forEach(item->{
                String fileSuffix = item.getFileSuffix();
                Integer type = annexFileEnumMap.get(fileSuffix);
                item.setFileType(type);
            });
            update.setAnnexFile(JSON.toJSONString(taskUpdateVo.getAnnexFiles()));
        });

        //更新
        taskService.updateById(update);
    }

    @DistributedLock(prefixKey = "task:", key = "#ids")
    @Override
    public void completeTask(String[] ids) {
        //完成任务
        List<TaskEntity> taskEntityList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getStatus, 1).in(TaskEntity::getId, ids));
        taskEntityList = taskEntityList.stream().filter(r -> r.getStatus() == 1).collect(Collectors.toList());
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

    @DistributedLock(prefixKey = "updateRemark", key = "updateVo")
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
