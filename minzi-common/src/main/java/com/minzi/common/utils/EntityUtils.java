package com.minzi.common.utils;


import java.lang.reflect.Field;
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
}
