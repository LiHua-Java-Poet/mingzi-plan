package com.minzi.plan.controller;


import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getUserList")
    public List<UserEntity> getUserList(){
        return userService.getList();
    }

}
