package com.minzi.common.utils;

import java.util.List;
import java.util.function.Function;

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

    // 定义一个方法，接收对象和一个方法引用，返回方法的值
    public static <T, R> R getFieldValue(T object, Function<T, R> methodReference) {
        if (object == null) {
            return null;
        }
        // 调用方法引用并返回结果
        return methodReference.apply(object);
    }
}
