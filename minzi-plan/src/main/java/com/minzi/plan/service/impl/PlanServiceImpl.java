package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.tools.EntityAct;
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
import org.springframework.stereotype.Service;
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


    @Override
    public Wrapper<PlanEntity> getListCondition(Map<String, Object> params) {
        LambdaQueryWrapper<PlanEntity> wrapper = new LambdaQueryWrapper<>();

        Object id = params.get("id");
        wrapper.eq(!StringUtils.isEmpty(id), PlanEntity::getId, id);

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
        EntityUtils.copySameFields(planSaveVo,entity);
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
        entityAct.oneToMany(entity,PlanEntity::getTaskEntityList);
        EntityUtils.copySameFields(entity, to);
        List<TaskEntity> taskEntityList = entity.getTaskEntityList();

        //拿到进行中的任务数
        to.setTowardProgress((int) taskEntityList.stream().filter(b -> b.getStatus() == 1).count());
        return to;
    }

    @Override
    public void update(PlanUpdateVo planUpdateVo) {

    }

    @Override
    public void delete(String[] ids) {

    }
}
