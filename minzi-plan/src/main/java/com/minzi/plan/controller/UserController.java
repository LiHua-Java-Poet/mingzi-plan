package com.minzi.plan.controller;


import com.minzi.common.core.R;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.vo.user.UserLoginVo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

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
