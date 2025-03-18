package com.minzi.common.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 策略Map
 */
public class ConsumerMap {

    /**
     * 策略集合
     */
    Map<String, Consumer<ParamsContext>> strategyMap = new HashMap<>();

    /**
     * 存入策略
     *
     * @param key      策略标识
     * @param consumer 方法
     */
    public void putConsumer(String key, Consumer<ParamsContext> consumer) {
        strategyMap.put(key, consumer);
    }

    /**
     * 获取到对应的策略方法
     *
     * @param key 策略标识
     * @return 返回的方法
     */
    public Consumer<ParamsContext> getConsumer(String key) {
        return strategyMap.get(key);
    }

}
