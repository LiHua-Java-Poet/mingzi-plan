package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.taskLog.TaskLogInfoTo;
import com.minzi.plan.model.to.taskLog.TaskLogListTo;
import com.minzi.plan.model.vo.taskLog.TaskLogSaveVo;
import com.minzi.plan.model.vo.taskLog.TaskLogUpdateVo;
import com.minzi.plan.service.TaskLogService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/taskLog")
public class TaskLogController {

    @Resource
    private TaskLogService taskLogService;


    @ApiOperation(value = "任务日志表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<TaskLogListTo> all = taskLogService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = taskLogService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "任务日志表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        TaskLogInfoTo one = taskLogService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存任务日志表")
    @PostMapping("/save")
    public R save(@RequestBody TaskLogSaveVo vo) {
        taskLogService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新任务日志表")
    @PostMapping("/update")
    public R update(@RequestBody TaskLogUpdateVo vo) {
        taskLogService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除任务日志表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        taskLogService.delete(ids);
        return R.ok();
    }
}
