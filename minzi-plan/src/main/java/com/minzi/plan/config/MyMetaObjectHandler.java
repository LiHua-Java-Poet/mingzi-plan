package com.minzi.plan.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时自动填充 createTime 字段
        this.strictInsertFill(metaObject, "createTime", Integer.class, (int) (System.currentTimeMillis() / 1000));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时不需要填充 createTime 字段
    }
}
