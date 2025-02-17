package com.minzi.plan.config;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义拦截器：当 IN 条件为空时，自动替换为 IN (-1)
 */
public class EmptyInInterceptor implements InnerInterceptor {

    // 匹配 SQL 中形如 "in ()" 的空 IN 条件（不区分大小写）
    private static final Pattern EMPTY_IN_PATTERN = Pattern.compile("(?i)\\bin\\s*\\(\\s*\\)");

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        String originalSql = boundSql.getSql();
        Matcher matcher = EMPTY_IN_PATTERN.matcher(originalSql);
        if (matcher.find()) {
            // 将空 IN 条件替换为 IN (-1)
            String newSql = matcher.replaceAll("IN (-1)");
            try {
                // 通过反射修改 BoundSql 内部的 sql 属性
                Field field = BoundSql.class.getDeclaredField("sql");
                field.setAccessible(true);
                field.set(boundSql, newSql);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("EmptyInInterceptor 修改 SQL 失败", e);
            }
        }
    }
}
