package com.minzi.common.core.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public class Query<T> {
    public Query() {
    }

    public IPage<T> getPage(Map<String, Object> params) {
        long curPage = 1L;
        long limit = 1L;
        if (params.get("page") != null) {
            curPage = Long.parseLong(params.get("page").toString());
        }
        if (params.get("limit") != null) {
            limit = Long.parseLong(params.get("limit").toString());
        }
        IPage<T> page = new Page(curPage, limit);
        params.put("page", page);
        return page;
    }
}
