package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.dao.PlanDao;
import com.minzi.plan.model.entity.PlanEntity;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.service.PlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanDao, PlanEntity> implements PlanService {

    @Resource
    private PlanService planService;

    @Override
    public List<PlanListTo> getList() {
        List<PlanEntity> list = planService.list();
        return list.stream().map(item->{
            PlanListTo to=new PlanListTo();
            EntityUtils.copySameFields(item,to);
            return to;
        }).collect(Collectors.toList());
    }
}
