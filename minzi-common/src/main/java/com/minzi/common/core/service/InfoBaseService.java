package com.minzi.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.common.core.query.R;

import java.util.Map;

public interface InfoBaseService<T, INFO_TO> extends IService<T> {


    default INFO_TO getOne(Long id) {
        T byIdOne = this.getById(id);
        R.dataParamsAssert(byIdOne == null, "不存在的数据");
        return this.formatOne(this.getById(id));
    }

    Wrapper<T> getOneCondition(Map<String, Object> params);

    INFO_TO formatOne(T entity);

}
