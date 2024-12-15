package com.minzi.plan.controller;


import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/User")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getUserList")
    public List<UserEntity> SendNoteMail(){
        return userService.getList();
    }

}
