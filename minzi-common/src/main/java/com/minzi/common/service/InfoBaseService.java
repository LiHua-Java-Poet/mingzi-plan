package com.minzi.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.common.core.PageUtils;
import com.minzi.common.core.Query;

import java.util.List;
import java.util.Map;

public interface InfoBaseService<T,INFO_TO> extends IService<T> {


    default INFO_TO getOne(Long id) {
        return this.formatOne(this.getById(id));
    }

    Wrapper<T> getOneCondition(Map<String, Object> params);

    INFO_TO formatOne(T entity);

}
