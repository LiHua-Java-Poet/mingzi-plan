package com.minzi.common.utils;

import com.baomidou.mybatisplus.extension.api.R;
import com.minzi.common.exception.BaseRuntimeException;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * 判断对象是否为null
     *
     * @return 返回结果
     */
    public static <T> boolean objectIsNull(T o) {
        return o == null;
    }

    /**
     * 判断对象是否为null
     *
     * @return 返回结果
     */
    public static <T> boolean objectIsNull(Collection<T> o) {
        return o == null || o.isEmpty();
    }

    public static <T, R> String getFieldName(PropertyFunc<T, R> methodReference) {
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
            throw new BaseRuntimeException("Failed to extract property name from getter", e);
        }
    }


    public static <T, R> R getFieldValue(Object obj, PropertyFunc<T, R> methodReference) {
        if (obj == null) return null;
        String fieldName = getFieldName(methodReference);
        Class<?> targetClass = obj.getClass();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (R) field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
