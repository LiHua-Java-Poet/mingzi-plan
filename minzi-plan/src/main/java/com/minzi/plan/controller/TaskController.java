package com.minzi.plan.controller;


import com.minzi.common.core.PageUtils;
import com.minzi.common.core.R;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/task")
@Api(tags = "任务管理")
public class TaskController {

    @Resource
    private TaskService taskService;


    @ApiOperation(value = "获取到用户的计划列表", response = TaskListTo.class)
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params) {
        if (StringUtils.isEmpty(params.get("page"))){
            List<TaskListTo> all = taskService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = taskService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "获取到计划的信息", notes = "获取到计划的信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        TaskInfoTo one = taskService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存新增计划", notes = "获取到计划的信息")
    @PostMapping("/save")
    public R save(@RequestBody TaskSaveVo vo) {
        taskService.add(vo);
        return R.ok().setData(null);
    }
}
