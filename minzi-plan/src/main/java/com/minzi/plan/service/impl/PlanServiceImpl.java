package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.service.BaseService;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.dao.PlanDao;
import com.minzi.plan.model.entity.PlanEntity;
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


    @Override
    public Wrapper<PlanEntity> getListCondition(Map<String, Object> params) {
        LambdaQueryWrapper<PlanEntity> wrapper = new LambdaQueryWrapper<>();

        Object id = params.get("id");
        wrapper.eq(!StringUtils.isEmpty(id),PlanEntity::getId,id);

        return wrapper;
    }

    @Override
    public List<PlanListTo> formatList(List<PlanEntity> list) {
        return list.stream().map(item -> {
            PlanListTo to = new PlanListTo();
            EntityUtils.copySameFields(item,to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(PlanSaveVo planSaveVo) {

    }
}
