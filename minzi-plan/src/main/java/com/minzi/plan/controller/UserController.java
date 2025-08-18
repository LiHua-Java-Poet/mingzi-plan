package com.minzi.plan.controller;


import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.user.UserLoginTo;
import com.minzi.plan.model.vo.user.UserLoginVo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "获取到用户列表", response = UserLoginTo.class)
    @GetMapping("/getUserList")
    public R getUserList() {
        List<UserEntity> list = userService.getList();
        return R.ok().setData(list);
    }

    @GetMapping("/demo")
    public String demo() {
        return "hello";
    }

    @PostMapping("/reg")
    public R reg(@RequestBody UserRegVo vo) {
        userService.reg(vo);
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo vo) {
        return userService.login(vo.getUserName(), vo.getPassword());
    }

}
