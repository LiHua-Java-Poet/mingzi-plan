package com.minzi.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minzi.common.core.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}
