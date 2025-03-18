package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.model.vo.plan.PlanSaveVo;
import com.minzi.plan.model.vo.plan.PlanUpdateVo;
import com.minzi.plan.service.PlanService;
import io.swagger.annotations.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/plan")
@Api(tags = "计划管理")
public class PlanController {

    @Resource
    private PlanService planService;


    @ApiOperation(value = "获取到用户的计划列表", response = PlanListTo.class)
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<PlanListTo> all = planService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = planService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "获取到计划的信息", notes = "获取到计划的信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        PlanInfoTo one = planService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存新增计划", notes = "获取到计划的信息")
    @PostMapping("/save")
    public R save(@RequestBody PlanSaveVo vo) {
        planService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新计划", notes = "更新计划")
    @PostMapping("/update")
    public R update(@RequestBody PlanUpdateVo vo) {
        planService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "取消计划", notes = "更新计划")
    @PostMapping("/cancelPlan")
    public R cancelPlan(@RequestBody String[] ids) {
        planService.cancelPlan(ids);
        return R.ok();
    }

    @ApiOperation(value = "下发多个任务", notes = "下发多个任务")
    @PostMapping("/deliver")
    public R deliver(@RequestBody String[] ids) {
        Long id = Long.parseLong(ids[0]);
        planService.deliver(id);
        return R.ok().setData(null);
    }


}
