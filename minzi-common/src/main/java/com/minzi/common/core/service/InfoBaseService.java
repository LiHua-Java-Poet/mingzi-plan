package com.minzi.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface InfoBaseService<T,INFO_TO> extends IService<T> {


    default INFO_TO getOne(Long id) {
        return this.formatOne(this.getById(id));
    }

    Wrapper<T> getOneCondition(Map<String, Object> params);

    INFO_TO formatOne(T entity);

}
