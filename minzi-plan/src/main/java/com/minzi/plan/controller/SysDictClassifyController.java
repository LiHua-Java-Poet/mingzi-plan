package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyInfoTo;
import com.minzi.plan.model.to.sysDictClassify.SysDictClassifyListTo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifySaveVo;
import com.minzi.plan.model.vo.sysDictClassify.SysDictClassifyUpdateVo;
import com.minzi.plan.service.SysDictClassifyService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysDictClassify")
public class SysDictClassifyController {

    @Resource
    private SysDictClassifyService sysDictClassifyService;


    @ApiOperation(value = "字典分类表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysDictClassifyListTo> all = sysDictClassifyService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysDictClassifyService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "字典分类表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysDictClassifyInfoTo one = sysDictClassifyService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存字典分类表")
    @PostMapping("/save")
    public R save(@RequestBody SysDictClassifySaveVo vo) {
        sysDictClassifyService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新字典分类表")
    @PostMapping("/update")
    public R update(@RequestBody SysDictClassifyUpdateVo vo) {
        sysDictClassifyService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除字典分类表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysDictClassifyService.delete(ids);
        return R.ok();
    }
}
