package com.minzi.plan.controller;


import com.minzi.common.core.R;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.service.PlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<PlanListTo> list = planService.getList();
        return R.ok().setData(list);
    }

    @GetMapping("/list2")
    public R list2(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<PlanListTo> list = planService.getList();
        return R.ok().setData(list);
    }
}
