package com.minzi.plan.aop;

import com.minzi.common.core.query.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
public class DataSourceConnectionAspect {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConnectionAspect.class);

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    /**
     * 切入所有实现javax.sql.DataSource接口的getConnection方法
     */
    @Around("execution(com.alibaba.druid.pool.DruidPooledConnection com.alibaba.druid.pool.DruidDataSource.getConnection(..))")
    public Object aroundGetConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String dataSourceClass = joinPoint.getTarget().getClass().getName();

        try {
            Connection connection = (Connection) joinPoint.proceed();
            long cost = System.currentTimeMillis() - startTime;

            logger.info("DataSource连接获取成功 | 来源: {} | 耗时: {}ms | 连接Hash: {}",
                    dataSourceClass, cost, System.identityHashCode(connection));

            return connection;
        } catch (SQLException e) {
            logger.error("DataSource连接获取失败 | 来源: {}", dataSourceClass, e);
            throw e;
        }
    }

    @Around("execution(com.minzi.common.core.query.R com.minzi.plan.service.impl.UserServiceImpl.login(..))")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            R r = (R) joinPoint.proceed();
            long cost = System.currentTimeMillis() - startTime;

            logger.info("DataSource连接获取成功 | 来源: {} | 耗时: {}ms | 连接Hash: {}");

            return r;
        } catch (SQLException e) {
            throw e;
        }
    }
}