package com.minzi.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.common.core.R;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.vo.user.UserRegVo;

import java.util.List;

public interface UserService extends IService<UserEntity> {

    List<UserEntity> getList();

    R login(String userName, String password);

    void reg(UserRegVo vo);
}
