package com.minzi.common.tools;

import com.minzi.common.annotation.OneToOne;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.common.utils.PropertyFunc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public interface OneToOneAct {


    default <T> void oneToOne(T item, PropertyFunc<T, ?> mapFieldFunc) {
        if (item == null ) return;
        String fieldValue = ObjectUtils.getFieldValue(mapFieldFunc);
        try {
            Class<?> target = item.getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToOne annotation = declaredField.getAnnotation(OneToOne.class);
            Class<?> targetService = annotation.targetService();
            this.OneToOneSetValue(Arrays.asList(item), annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    default <T> void oneToOne(List<T> itemList, PropertyFunc<T, ?> mapFieldFunc) {
        if (itemList == null || itemList.isEmpty()) return;
        String fieldValue = ObjectUtils.getFieldValue(mapFieldFunc);
        try {
            Class<?> target = itemList.get(0).getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToOne annotation = declaredField.getAnnotation(OneToOne.class);
            Class<?> targetService = annotation.targetService();
            this.OneToOneSetValue(itemList, annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    <T> void OneToOneSetValue(List<T> itemList, com.minzi.common.annotation.OneToOne annotation, Class<?> targetService, Field declaredField) throws NoSuchFieldException, IllegalAccessException;

}
