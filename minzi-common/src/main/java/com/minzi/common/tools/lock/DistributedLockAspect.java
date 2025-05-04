package com.minzi.common.tools.lock;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
@Aspect
public class DistributedLockAspect {

    @Resource
    private RedissonClient redissonClient;

    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.minzi.common.tools.lock.DistributedLock)")//如果不加@annotation,那么拦截mapper包下所有的方法,加了就拦截这个包下的加了这个注解的方法
    public void autoFillPointCut(){}

    @Around("autoFillPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method =((MethodSignature) joinPoint.getSignature()).getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        // 解析SpEL表达式获取锁的key
        String key = parseSpel(distributedLock.prefixKey(), distributedLock.key(), joinPoint);
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock();
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 解析SpEL表达式
     */
    private String parseSpel(String spel, String key, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法参数名
        String[] params = discoverer.getParameterNames(method);
        if (params == null || params.length == 0) {
            return spel;
        }

        //拿到方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < params.length; i++) {
            spel = params[i].equals(key) ? spel + args[i].toString() : spel;
        }
        return spel;
    }
}
