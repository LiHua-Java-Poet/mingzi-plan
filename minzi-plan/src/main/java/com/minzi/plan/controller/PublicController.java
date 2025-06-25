package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.plan.PlanListTo;
import com.minzi.plan.model.to.publish.UserDataTo;
import com.minzi.plan.service.PlanService;
import com.minzi.plan.service.PublicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/public")
@Api(tags = "公共接口")
public class PublicController {

    @Resource
    private PublicService publicService;

    @ApiOperation(value = "获取到用户的概览信息", response = UserDataTo.class)
    @GetMapping("/getUserDataTo")
    public R getUserDataTo(@RequestParam Map<String, Object> params) {
        UserDataTo userDataTo = publicService.getUserDataTo();
        return R.ok().setData(userDataTo);
    }
}
