package com.minzi.plan.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 数据切面
 */
@Aspect
@Component
public class DataAspect {

    // 切点：匹配 UserService 接口的所有方法
    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.insert(..))")
    public void insert() {}

    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.updateById(..))")
    public void updateById() {}

    // 前置通知
    @Before("insert()")
    public void beforeMethod(JoinPoint joinPoint) {
        //这个是切入到了所有的插入方法
        System.out.println("调用前: " + joinPoint.getSignature().getName());
    }
}
