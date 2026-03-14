package com.minzi.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minzi.plan.model.entity.TaskLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogDao extends BaseMapper<TaskLogEntity> {
}
