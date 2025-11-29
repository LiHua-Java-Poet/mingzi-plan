package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.common.core.tools.cache.CacheClean;
import com.minzi.plan.model.to.session.SessionInfoTo;
import com.minzi.plan.model.to.session.SessionListTo;
import com.minzi.plan.model.vo.session.SessionSaveVo;
import com.minzi.plan.model.vo.session.SessionUpdateVo;
import com.minzi.plan.service.SessionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/session")
public class SessionController {

    @Resource
    private SessionService sessionService;


    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<SessionListTo> all = sessionService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = sessionService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        SessionInfoTo one = sessionService.getOne(id);
        return R.ok().setData(one);
    }

    @CacheClean(business = "session")
    @PostMapping("/save")
    public R save(@RequestBody SessionSaveVo vo) {
        sessionService.add(vo);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody SessionUpdateVo vo) {
        sessionService.update(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除消息")
    @PostMapping("/delete")
    @CacheClean(business = "session")
    public R delete(@RequestBody String[] ids) {
        sessionService.delete(ids);
        return R.ok();
    }
}
