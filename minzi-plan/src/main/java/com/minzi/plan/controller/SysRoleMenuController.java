package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.vo.sysRoleMenu.PermissionVo;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuInfoTo;
import com.minzi.plan.model.to.sysRoleMenu.SysRoleMenuListTo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuSaveVo;
import com.minzi.plan.model.vo.sysRoleMenu.SysRoleMenuUpdateVo;
import com.minzi.plan.service.SysRoleMenuService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysRoleMenu")
public class SysRoleMenuController {

    @Resource
    private SysRoleMenuService sysRoleMenuService;


    @ApiOperation(value = "角色菜单表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysRoleMenuListTo> all = sysRoleMenuService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysRoleMenuService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "角色菜单表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysRoleMenuInfoTo one = sysRoleMenuService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存角色菜单表")
    @PostMapping("/save")
    public R save(@RequestBody SysRoleMenuSaveVo vo) {
        sysRoleMenuService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "保存角色菜单表")
    @PostMapping("/addList")
    public R addList(@RequestBody List<SysRoleMenuSaveVo> vo) {
        sysRoleMenuService.addList(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新角色菜单表")
    @PostMapping("/update")
    public R update(@RequestBody SysRoleMenuUpdateVo vo) {
        sysRoleMenuService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除角色菜单表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysRoleMenuService.delete(ids);
        return R.ok();
    }

    @ApiOperation(value = "删除角色菜单表")
    @PostMapping("/deletePermission")
    public R deletePermission(@RequestBody PermissionVo vo) {
        sysRoleMenuService.deletePermission(vo);
        return R.ok();
    }
}
