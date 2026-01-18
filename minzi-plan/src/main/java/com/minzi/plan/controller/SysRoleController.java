package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import io.swagger.annotations.ApiOperation;
import com.minzi.plan.model.to.sysRole.SysRoleInfoTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import com.minzi.plan.model.vo.sysRole.SysRoleSaveVo;
import com.minzi.plan.model.vo.sysRole.SysRoleUpdateVo;
import com.minzi.plan.service.SysRoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;


    @ApiOperation(value = "角色表列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysRoleListTo> all = sysRoleService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysRoleService.queryPage(params);
        return R.ok().setData(pageUtils);
    }


    @ApiOperation(value = "角色表信息")
    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysRoleInfoTo one = sysRoleService.getOne(id);
        return R.ok().setData(one);
    }

    @ApiOperation(value = "保存角色表")
    @PostMapping("/save")
    public R save(@RequestBody SysRoleSaveVo vo) {
        sysRoleService.add(vo);
        return R.ok();
    }

    @ApiOperation(value = "更新角色表")
    @PostMapping("/update")
    public R update(@RequestBody SysRoleUpdateVo vo) {
        sysRoleService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除角色表")
    @PostMapping("/delete")
    public R update(@RequestBody String[] ids) {
        sysRoleService.delete(ids);
        return R.ok();
    }
}
