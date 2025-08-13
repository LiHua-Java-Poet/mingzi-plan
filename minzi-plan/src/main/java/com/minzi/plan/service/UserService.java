package com.minzi.plan.service;

import com.minzi.common.core.query.R;
import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.to.user.UserListTo;
import com.minzi.plan.model.to.user.UserInfoTo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.model.vo.user.UserSaveVo;
import com.minzi.plan.model.vo.user.UserUpdateVo;

import java.util.List;

public interface UserService extends BaseService<UserEntity, UserListTo, UserInfoTo, UserSaveVo, UserUpdateVo> {

    List<UserEntity> getList();

    R login(String userName, String password);

    void reg(UserRegVo vo);
}
