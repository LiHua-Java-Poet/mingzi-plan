package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysDict.SysDictInfoTo;
import com.minzi.plan.model.to.sysDict.SysDictListTo;
import com.minzi.plan.model.vo.sysDict.SysDictSaveVo;
import com.minzi.plan.model.vo.sysDict.SysDictUpdateVo;
import com.minzi.plan.service.SysDictService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysDict")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;


    @ApiOperation(value = "字典表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysDictListTo> all = sysDictService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysDictService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "字典表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysDictInfoTo one = sysDictService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存字典表")
    @PostMapping("/save")
    public R save(@RequestBody SysDictSaveVo vo) {
        sysDictService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新字典表")
    @PostMapping("/update")
    public R update(@RequestBody SysDictUpdateVo vo) {
        sysDictService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除字典表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysDictService.delete(ids);
        return R.ok();
    }
}
