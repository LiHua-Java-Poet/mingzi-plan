package com.minzi.common.core.map;


import com.minzi.common.utils.ObjectUtils;
import com.minzi.common.utils.PropertyFunc;

import java.util.HashMap;
import java.util.Map;

public class LambdaHashMap<K, V> extends HashMap<K, V> {

    public LambdaHashMap(Map<K, V> params) {
        super(params);
    }

    public <T, R> Object get(PropertyFunc<T, R> methodReference) {
        String fieldName = ObjectUtils.getFieldName(methodReference);
        return super.get(fieldName);
    }

    public Object get(String methodReference) {
        return super.get(methodReference);
    }

    // 修改方法名称
    public <T, R> void put(PropertyFunc<T, R> methodReference, Object value) {
        String fieldName = ObjectUtils.getFieldName(methodReference);
        super.put((K) fieldName, (V) value); // 注意类型转换
    }

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

}
