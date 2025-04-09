package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.common.SysConfigContext;
import com.minzi.plan.model.to.sysConfig.SysConfigInfoTo;
import com.minzi.plan.model.to.sysConfig.SysConfigListTo;
import com.minzi.plan.model.vo.sysConfig.SysConfigSaveVo;
import com.minzi.plan.model.vo.sysConfig.SysConfigUpdateVo;
import com.minzi.plan.service.SysConfigService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/sysConfig")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private SysConfigContext sysConfigContext;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SysConfigListTo> all = sysConfigService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sysConfigService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @GetMapping("/getConfigName")
    public R getConfigName(@RequestParam Map<String, Object> params) {
        String key = sysConfigContext.getConfigContext(params.get("key").toString());
        return R.ok().setData(key);
    }

    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SysConfigInfoTo one = sysConfigService.getOne(id);
        return R.ok().setData(one);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysConfigSaveVo vo) {
        sysConfigService.add(vo);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody SysConfigUpdateVo vo) {
        sysConfigService.update(vo);
        return R.ok();
    }
}
