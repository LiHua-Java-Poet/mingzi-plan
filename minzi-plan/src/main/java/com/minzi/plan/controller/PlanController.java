package com.minzi.plan.controller;


import com.minzi.common.core.PageUtils;
import com.minzi.common.core.R;
import com.minzi.plan.model.to.plan.PlanInfoTo;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.model.vo.plan.PlanSaveVo;
import com.minzi.plan.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/plan")
@Api(tags = "示例控制器")
public class PlanController {

    @Resource
    private PlanService planService;


    @ApiOperation(value = "获取示例信息", notes = "返回示例信息")
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params) {
        if (StringUtils.isEmpty(params.get("page"))){
            List<PlanListTo> all = planService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = planService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        return R.ok().setData(null);
    }

    @PostMapping("/save")
    public R save(@RequestBody PlanSaveVo vo) {
        planService.add(vo);
        return R.ok().setData(null);
    }


}
