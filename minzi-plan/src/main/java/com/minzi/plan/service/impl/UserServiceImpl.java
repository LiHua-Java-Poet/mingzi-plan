package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.plan.dao.UserDao;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao,UserEntity> implements UserService {


    @Resource
    private UserService userService;


    @Override
    public List<UserEntity> getList() {
        return userService.list();
    }
}
