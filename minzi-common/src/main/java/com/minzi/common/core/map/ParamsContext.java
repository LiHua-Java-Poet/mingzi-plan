package com.minzi.common.core.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文对象
 */
public class ParamsContext {

    // 使用 ConcurrentHashMap 保证线程安全
    private final Map<String, Object> contextMap = new ConcurrentHashMap<>();

    /**
     * 存储上下文信息
     *
     * @param key   上下文信息的键
     * @param value 上下文信息的值
     */
    public void put(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        contextMap.put(key, value);
    }

    /**
     * 获取上下文信息，并转换为指定类型
     *
     * @param key   上下文信息的键
     * @param clazz 期望的返回值类型
     * @return 上下文信息的值，转换为指定类型
     * @throws IllegalArgumentException 如果值不存在或类型不匹配
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = contextMap.get(key);
        if (value == null) {
            throw new IllegalArgumentException("No value found for key: " + key);
        }
        if (!clazz.isInstance(value)) {
            throw new IllegalArgumentException("Value for key '" + key + "' is not of type " + clazz.getName());
        }
        return clazz.cast(value);
    }

    /**
     * 获取上下文信息，并转换为指定类型
     *
     * @param key   上下文信息的键
     * @return 上下文信息的值，转换为指定类型
     * @throws IllegalArgumentException 如果值不存在或类型不匹配
     */
    public Object get(String key) {
        Object value = contextMap.get(key);
        if (value == null) {
            throw new IllegalArgumentException("No value found for key: " + key);
        }
        return value;
    }

    /**
     * 获取上下文信息，如果不存在或类型不匹配则返回默认值
     *
     * @param key          上下文信息的键
     * @param clazz        期望的返回值类型
     * @param defaultValue 默认值
     * @return 上下文信息的值，转换为指定类型；如果不存在或类型不匹配则返回默认值
     */
    public <T> T getOrDefault(String key, Class<T> clazz, T defaultValue) {
        try {
            return get(key, clazz);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * 删除上下文信息
     *
     * @param key 上下文信息的键
     */
    public void remove(String key) {
        contextMap.remove(key);
    }

    /**
     * 检查是否包含某个上下文信息
     *
     * @param key 上下文信息的键
     * @return 如果包含则返回 true，否则返回 false
     */
    public boolean contains(String key) {
        return contextMap.containsKey(key);
    }

    /**
     * 清空所有上下文信息
     */
    public void clear() {
        contextMap.clear();
    }

    /**
     * 获取上下文容器的大小
     *
     * @return 上下文容器中存储的键值对数量
     */
    public int size() {
        return contextMap.size();
    }
}
