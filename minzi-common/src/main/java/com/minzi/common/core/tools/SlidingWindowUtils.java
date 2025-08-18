package com.minzi.common.core.tools;


import com.minzi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SlidingWindowUtils  {

    @Value("${window.limit}")
    private Integer limit;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    public boolean allowRequest(String key) {
        if (limit == null) limit = 100;
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
        Integer currentTime = DateUtils.currentDateTime();
        Integer windowStart = currentTime - 60;
        stringStringZSetOperations.removeRangeByScore(key, Double.MIN_VALUE, windowStart);
        Long size = stringStringZSetOperations.size(key);
        if (size < limit) {
            stringStringZSetOperations.add(key,currentTime.toString(),currentTime);
            return true;
        }
        return false;
    }
}
