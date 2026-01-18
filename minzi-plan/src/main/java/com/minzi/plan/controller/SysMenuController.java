package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysMenu.SysMenuInfoTo;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.vo.sysMenu.SysMenuSaveVo;
import com.minzi.plan.model.vo.sysMenu.SysMenuUpdateVo;
import com.minzi.plan.service.SysMenuService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysMenu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;


    @ApiOperation(value = "菜单目录名列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysMenuListTo> all = sysMenuService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysMenuService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "菜单目录名信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysMenuInfoTo one = sysMenuService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存菜单目录名")
    @PostMapping("/save")
    public R save(@RequestBody SysMenuSaveVo vo) {
        sysMenuService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新菜单目录名")
    @PostMapping("/update")
    public R update(@RequestBody SysMenuUpdateVo vo) {
        sysMenuService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除菜单目录名")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysMenuService.delete(ids);
        return R.ok();
    }
}
