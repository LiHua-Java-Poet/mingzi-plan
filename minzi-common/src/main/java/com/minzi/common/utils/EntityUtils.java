package com.minzi.common.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EntityUtils<K, T> {

    public Map<K, T> get(List<T> list, Function function) {
        Map<K, T> result = new HashMap<>();
        list.forEach(item -> {
            Object apply = function.apply(item);
            result.put((K) apply, item);
        });
        return result;
    }

    /**
     * 将 source 对象中与 target 对象字段名和类型相同的属性复制到 target 对象中。
     *
     * @param source 源对象
     * @param target 目标对象
     * @throws IllegalAccessException 如果字段无法访问
     */
    public static void copySameFields(Object source, Object target) {
        if (source == null) return;
        // 获取源对象和目标对象的 Class 对象
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 获取源对象的所有字段（包括私有字段）
        Field[] sourceFields = sourceClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            // 设置字段可访问（包括私有字段）
            sourceField.setAccessible(true);

            // 获取源字段的名称
            String fieldName = sourceField.getName();

            try {
                // 获取目标对象中同名字段
                Field targetField = targetClass.getDeclaredField(fieldName);

                // 设置目标字段可访问
                targetField.setAccessible(true);

                // 检查字段类型是否相同
                if (sourceField.getType().equals(targetField.getType())) {
                    try {
                        // 获取源字段的值
                        Object value = sourceField.get(source);
                        if (value == null) continue;
                        // 将值设置到目标字段
                        targetField.set(target, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (NoSuchFieldException e) {
                // 如果目标对象中没有同名字段，则忽略
                continue;
            }
        }
    }

    /**
     * 将对象列表转换为以指定属性值为键的 Map
     *
     * @param list     对象列表
     * @param keyField 指定属性的 getter 方法引用
     * @return Map<属性值类型, 对象>
     */
    public static <T, R> Map<R, T> resortEntityByColumnLevel1(List<T> list, PropertyFunc<T, R> keyField) {
        String fieldValue = ObjectUtils.getFieldValue(keyField);
        Map<R, T> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        for (T obj : list) {
            R key = getPropertyValue(obj, fieldValue);
            resultMap.put(key, obj);
        }
        return resultMap;
    }

    public static <T, R> Map<R, T> resortEntityByColumnLevel1(List<T> list, String keyField) {
        Map<R, T> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        for (T obj : list) {
            R key = getPropertyValue(obj, StringUtils.underscoreToCamelCase(keyField));
            resultMap.put(key, obj);
        }
        return resultMap;
    }

    public static <T, R> Map<R, List<T>> resortEntityByColumnLevel2(List<T> list, PropertyFunc<T, R> keyField) {
        Map<R, List<T>> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        String fieldValue = ObjectUtils.getFieldValue(keyField);
        return getrListMap(list, resultMap, fieldValue);
    }

    public static <T, R> Map<R, List<T>> resortEntityByColumnLevel2(List<T> list, String keyField) {
        Map<R, List<T>> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        return getrListMap(list, resultMap, keyField);
    }

    public static <T, K, V> Map<K, V> resortEntityByColumnLevel3(List<T> list, PropertyFunc<T, K> keyField, PropertyFunc<T, V> valueField) {
        String keyFieldValue = ObjectUtils.getFieldValue(keyField);
        String valueFieldValue = ObjectUtils.getFieldValue(valueField);

        Map<K, V> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        for (T obj : list) {
            K key = getPropertyValue(obj, keyFieldValue);
            V value = getPropertyValue(obj, valueFieldValue);
            resultMap.put(key, value);
        }
        return resultMap;
    }

    public static <T, K,V> Map<K, List<V>> resortEntityByColumnLevel4(List<T> list, PropertyFunc<T, K> keyField, PropertyFunc<T, V> valueField) {
        Map<K, List<V>> resultMap = new HashMap<>();
        if (list == null || list.isEmpty()) return resultMap;
        String keyFieldValue = ObjectUtils.getFieldValue(keyField);
        String valueFieldValue = ObjectUtils.getFieldValue(valueField);
        for (T obj : list) {
            K key = getPropertyValue(obj, StringUtils.underscoreToCamelCase(keyFieldValue));
            V value = getPropertyValue(obj, valueFieldValue);
            List<V> ts = resultMap.get(key);
            if (ts == null) {
                ts = new ArrayList<>();
            }
            ts.add(value);
            resultMap.put(key, ts);
        }
        return resultMap;
    }

    private static <T, R> Map<R, List<T>> getrListMap(List<T> list, Map<R, List<T>> resultMap, String fieldValue) {
        for (T obj : list) {
            R key = getPropertyValue(obj, StringUtils.underscoreToCamelCase(fieldValue));
            List<T> ts = resultMap.get(key);
            if (ts == null) {
                ts = new ArrayList<>();
            }
            ts.add(obj);
            resultMap.put(key, ts);
        }
        return resultMap;
    }

    /**
     * 通过反射获取对象属性值（优先调用 getter 方法）
     */
    private static <R> R getPropertyValue(Object obj, String propertyName) {
        try {
            // 1. 尝试调用 getter 方法
            String getterName = "get" + capitalize(propertyName);
            Method method = obj.getClass().getMethod(getterName);
            return (R) method.invoke(obj);
        } catch (NoSuchMethodException e) {
            // 2. 如果 getter 不存在，直接访问字段
            try {
                Field field = obj.getClass().getDeclaredField(propertyName);
                field.setAccessible(true); // 允许访问私有字段
                return (R) field.get(obj);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new IllegalArgumentException(
                        "属性 '" + propertyName + "' 在类 " + obj.getClass().getName() + " 中不存在或不可访问", ex);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("访问属性 '" + propertyName + "' 失败", e);
        }
    }

    /**
     * 首字母大写工具方法
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


}
