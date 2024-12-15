package com.minzi.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.plan.model.entity.UserEntity;

import java.util.List;

public interface UserService extends IService<UserEntity> {

    List<UserEntity> getList();
}
