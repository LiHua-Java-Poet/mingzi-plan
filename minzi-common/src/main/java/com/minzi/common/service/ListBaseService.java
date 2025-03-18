package com.minzi.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.Query;

import java.util.List;
import java.util.Map;

public interface ListBaseService<T, TO> extends IService<T> {

    default PageUtils queryPage(Map<String, Object> params) {
        IPage<T> page = this.page((new Query()).getPage(params), this.getListCondition(params));
        PageUtils pageUtils = new PageUtils(page);
        List<T> list = pageUtils.list();
        pageUtils.setList(this.formatList(list));
        return pageUtils;
    }

    default List<TO> all(Map<String, Object> params){
        return this.formatList(this.list(this.getListCondition(params)));
    }

    Wrapper<T> getListCondition(Map<String, Object> params);


    List<TO> formatList(List<T> list);
}
