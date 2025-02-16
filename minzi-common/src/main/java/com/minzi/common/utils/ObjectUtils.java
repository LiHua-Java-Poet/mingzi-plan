package com.minzi.common.utils;

import com.baomidou.mybatisplus.extension.api.R;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtils {

    /**
     * 判断列表是否为null
     *
     * @return 返回结果
     */
    static public boolean listIsNull(List<?> list) {
        return list == null || list.isEmpty();
    }


    /**
     * 判断对象是否为null
     *
     * @return 返回结果
     */
    boolean objectIsNull(Object o) {
        return o == null;
    }

    public static <T, R> String getFieldValue(PropertyFunc<T, R> methodReference) {
        try {
            // 获取序列化的Lambda
            Method writeReplace = methodReference.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) writeReplace.invoke(methodReference);

            // 从方法名中解析出属性名
            String methodName = lambda.getImplMethodName();
            if (methodName.startsWith("get")) {
                methodName = methodName.substring(3); // 去掉 "get"
            } else if (methodName.startsWith("is")) {
                methodName = methodName.substring(2); // 去掉 "is"
            }
            // 将首字母小写以符合 JavaBean 规范
            return Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract property name from getter", e);
        }
    }

    /**
     * 将对象列表转换为以指定属性值为键的 Map
     *
     * @param list           对象列表
     * @param methodReference 指定属性的 getter 方法引用
     * @return Map<属性值类型, 对象>
     */
    public static <T, R> Map<R, T> convert(List<T> list, PropertyFunc<T, R> methodReference) {
        String fieldValue = getFieldValue(methodReference);
        Map<R, T> resultMap = new HashMap<>();
        for (T obj : list) {
            R key = getPropertyValue(obj, fieldValue);
            resultMap.put(key, obj);
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
