package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.task.TaskInfoTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.vo.task.TaskSaveVo;
import com.minzi.plan.model.vo.task.TaskUpdateVo;
import com.minzi.plan.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = false,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "limit",value = "大小",required = false,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "status",value = "状态",required = false,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取到用户的任务列表", response = TaskListTo.class)
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params) {
        if (StringUtils.isEmpty(params.get("page"))){
            List<TaskListTo> all = taskService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = taskService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "获取到任务信息", notes = "获取到计划的信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        TaskInfoTo one = taskService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "获取到任务信息", notes = "获取到分享任务信息接口")
    @GetMapping("/shareTaskInfo")
    public R shareTaskInfo(@RequestParam String code) {
        TaskInfoTo one = taskService.shareTaskInfo(code);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "获取到对应的分享Code", notes = "获取到对应的分享Code")
    @GetMapping("/getShareCode")
    public R getShareCode(@RequestParam Long id) {
        String code = taskService.getShareCode(id);
        return R.ok().setData(code);
    }

    @ApiOperation(value = "分享修改任务")
    @PostMapping("/shareUpdate")
    public R shareUpdate(@RequestBody TaskUpdateVo vo) {
        taskService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "保存新增任务")
    @PostMapping("/save")
    public R save(@RequestBody TaskSaveVo vo) {
        taskService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "保存新增任务")
    @PostMapping("/update")
    public R update(@RequestBody TaskUpdateVo vo) {
        taskService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除任务")
    @PostMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        taskService.delete(ids);
        return R.ok();
    }

    @ApiOperation(value = "完成任务")
    @PostMapping("/completeTask")
    public R completeTask(@RequestBody String[] ids) {
        taskService.completeTask(ids);
        return R.ok();
    }

    @ApiOperation(value = "取消任务")
    @PostMapping("/cancelTask")
    public R cancelTask(@RequestBody String[] ids) {
        taskService.cancelTask(ids);
        return R.ok();
    }

    @ApiOperation(value = "获取到唯一码")
    @GetMapping("/getUniqueCode")
    public R getUniqueCode() {
        String one = taskService.getUniqueCode();
        return R.ok().setData(one);
    }

    @ApiOperation(value = "更新任务的备注内容")
    @PostMapping("/updateRemark")
    public R updateRemark(@RequestBody TaskUpdateVo updateVo) {
        taskService.updateRemark(updateVo);
        return R.ok();
    }
}
