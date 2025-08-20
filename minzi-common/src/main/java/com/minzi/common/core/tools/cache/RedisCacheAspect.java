package com.minzi.common.core.tools.cache;


import com.alibaba.fastjson.JSON;
import com.minzi.common.core.tools.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class RedisCacheAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserContext userContext;


    @Around("@annotation(cacheable)")
    public Object doCache(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
        // 1. 获取注解参数
        String business = cacheable.business();
        boolean needUserId = cacheable.userId();
        int arg = cacheable.arg();
        int ttl = cacheable.ttl();

        // 2. 取方法参数
        Object[] args = pjp.getArgs();

        // 3. 解析 SpEL 表达式（params 里可以写 #id、#p0 这种）
        String paramVal = "";

        //只有参数大于0的情况下才需要拿到参数作为键去拼接
        if (args.length > 0) {
            Object arg1 = args[arg];
            paramVal = arg1.toString();
        }

        // 4. 拼接 key
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(business);
        if (needUserId) {
            //这里需要拿到用户的id
            Long userId = userContext.getUserId();
            keyBuilder.append(":").append(userId);
        }
        if (!paramVal.isEmpty()) {
            keyBuilder.append(":").append(paramVal);
        }
        String key = keyBuilder.toString();

        // 5. 先查缓存
        String cacheValue = stringRedisTemplate.opsForValue().get(key);
        if (cacheValue != null) {
            // 命中缓存 -> 续期
            stringRedisTemplate.expire(key, ttl, TimeUnit.SECONDS);

            // 方法返回类型
            Class<?> returnType = ((org.aspectj.lang.reflect.MethodSignature) pjp.getSignature()).getReturnType();
            return JSON.parseObject(cacheValue, returnType);
        }

        // 6. 没有缓存 → 执行原方法
        Object result = pjp.proceed();

        // 7. 写入缓存
        if (result != null) {
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(result), ttl, TimeUnit.SECONDS);
        }

        return result;
    }

    /**
     * 清除缓存
     */
    @After("@annotation(cacheable)")
    public void doCacheClean(CacheClean cacheable) {
        // 1. 获取注解参数
        String business = cacheable.business();
        boolean needUserId = cacheable.userId();

        // 2. 拼接 key 前缀
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(business);
        if (needUserId) {
            Long userId = userContext.getUserId();
            keyBuilder.append(":").append(userId);
        }
        String prefix = keyBuilder.toString();

        // 3. 使用 keys() 扫描所有前缀匹配的 key
        Set<String> keys = stringRedisTemplate.keys(prefix + "*");

        // 4. 删除这些 key
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

}
