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

}
