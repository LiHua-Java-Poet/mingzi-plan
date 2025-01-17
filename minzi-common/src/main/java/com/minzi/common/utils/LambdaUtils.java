package com.minzi.common.utils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

public class LambdaUtils {

    public static <T> String getFieldName(PropertyFunc<T, ?> field) {
        Class<?> clazz = field.getClass();
        try {
            Method method = clazz.getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(field);
            return uncapitalize(lambda.getImplMethodName());
        } catch (Exception e) {
            if (!Object.class.isAssignableFrom(clazz.getSuperclass())) {
                return getFieldName(field);
            }
            throw new RuntimeException("current property is not exists");
        }
    }

    // 去掉方法的“get/is”前缀，首字母小写
    private static String uncapitalize(String str) {
        if (str == null || str.length() < 4) {
            return str;
        }
        String fieldName = str.startsWith("get") ?
                str.substring(3) : str.startsWith("is") ? str.substring(2) : str;
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }

}
