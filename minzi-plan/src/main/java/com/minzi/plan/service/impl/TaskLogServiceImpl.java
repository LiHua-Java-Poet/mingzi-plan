package com.minzi.plan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.AnnexFile;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.TaskLogDao;
import com.minzi.plan.model.entity.TaskLogEntity;
import com.minzi.plan.model.to.taskLog.TaskLogInfoTo;
import com.minzi.plan.model.to.taskLog.TaskLogListTo;
import com.minzi.plan.model.vo.taskLog.TaskLogSaveVo;
import com.minzi.plan.model.vo.taskLog.TaskLogUpdateVo;
import com.minzi.plan.service.TaskLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogDao, TaskLogEntity> implements TaskLogService {

    @Resource
    private TaskLogService taskLogService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<TaskLogEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<TaskLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TaskLogEntity::getId);

        Object id = lambdaHashMap.get(TaskLogEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), TaskLogEntity::getId, id);

        Object taskId = lambdaHashMap.get(TaskLogEntity::getTaskId);
        wrapper.eq(!StringUtils.isEmpty(taskId), TaskLogEntity::getTaskId, taskId);
        return wrapper;
    }

    @Override
    public List<TaskLogListTo> formatList(List<TaskLogEntity> list) {
        return list.stream().map(item -> {
            TaskLogListTo to = new TaskLogListTo();
            EntityUtils.copySameFields(item, to);
            //转换一下附件的格式
            String annexFile = item.getAnnexFile();
            if (!StringUtils.isEmpty(annexFile)) to.setAnnexFileList(JSONArray.parseArray(annexFile, AnnexFile.class));
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(TaskLogSaveVo taskLogSaveVo) {
        TaskLogEntity entity = new TaskLogEntity();
        EntityUtils.copySameFields(taskLogSaveVo, entity);
        //转换一下附件格式
        List<AnnexFile> annexFileList = taskLogSaveVo.getAnnexFileList();
        if (!ObjectUtils.objectIsNull(annexFileList)) entity.setAnnexFile(JSONObject.toJSONString(annexFileList));
        taskLogService.save(entity);
    }

    @Override
    public TaskLogInfoTo formatOne(TaskLogEntity entity) {
        TaskLogInfoTo to = new TaskLogInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(TaskLogUpdateVo UpdateVo) {
        TaskLogEntity entity = taskLogService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        entity.setUpdateTime(DateUtils.currentDateTime());
        taskLogService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {

        taskLogService.update(
                new LambdaUpdateWrapper<TaskLogEntity>()
                        .set(TaskLogEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(TaskLogEntity::getId, ids)
        );
    }

}