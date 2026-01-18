package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleInfoTo;
import com.minzi.plan.model.to.sysUserRole.SysUserRoleListTo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleSaveVo;
import com.minzi.plan.model.vo.sysUserRole.SysUserRoleUpdateVo;
import com.minzi.plan.service.SysUserRoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysUserRole")
public class SysUserRoleController {

    @Resource
    private SysUserRoleService sysUserRoleService;


    @ApiOperation(value = "用户角色授权表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysUserRoleListTo> all = sysUserRoleService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysUserRoleService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @ApiOperation(value = "用户角色授权表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysUserRoleInfoTo one = sysUserRoleService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存用户角色授权表")
    @PostMapping("/save")
    public R save(@RequestBody SysUserRoleSaveVo vo) {
        sysUserRoleService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新用户角色授权表")
    @PostMapping("/update")
    public R update(@RequestBody SysUserRoleUpdateVo vo) {
        sysUserRoleService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除用户角色授权表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysUserRoleService.delete(ids);
        return R.ok();
    }
}
