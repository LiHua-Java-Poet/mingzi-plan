package com.minzi.common.core.tools.resubmit;

import com.minzi.common.core.query.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ResubmitAspect {

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.minzi.common.core.tools.resubmit.Resubmit)")
    public void autoFillPointCut() {
    }

    @Around("autoFillPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Resubmit resubmit = method.getAnnotation(Resubmit.class);
        Class<?> voClass = resubmit.voClass();

        Object[] args = joinPoint.getArgs();
        Object targetArg = null;

        for (Object arg : args) {
            if (voClass.isInstance(arg)) {
                targetArg = arg;
                break;
            }
        }

        if (targetArg == null) {
            return joinPoint.proceed();
        }

        Field nameField;
        try {
            nameField = voClass.getDeclaredField("uniqueCode");
        } catch (NoSuchFieldException e) {
            return joinPoint.proceed();
        }

        nameField.setAccessible(true);
        Object uniqueCode = nameField.get(targetArg);

        R.dataParamsAssert(uniqueCode == null, "校验码不能为空");

        String codeStr = uniqueCode.toString();
        RLock lock = redissonClient.getLock("resubmit:lock:" + codeStr);

        boolean locked = false;
        try {
            // 尝试加锁，最多等待3秒，锁定10秒后自动释放
            locked = lock.tryLock(3, 10, TimeUnit.SECONDS);
            R.dataParamsAssert(!locked, "操作过于频繁，请稍后再试");

            // 加锁后判断 Redis 中是否已有该值
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            String value = valueOperations.get(codeStr);

            stringRedisTemplate.delete(codeStr);

            R.dataParamsAssert(value == null, "请不要重复提交");

            return joinPoint.proceed();
        } finally {
            // 确保加锁成功时才释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

