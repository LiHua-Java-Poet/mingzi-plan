package com.minzi.common.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minzi.common.annotation.OneToOne;
import com.minzi.common.service.BaseService;
import com.minzi.common.tools.utils.SpringContextUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.common.utils.PropertyFunc;
import com.minzi.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public interface OneToOneAct {


    default <T> void oneToOne(T item, PropertyFunc<T, ?> mapFieldFunc) {
        if (item == null ) return;
        String fieldValue = ObjectUtils.getFieldName(mapFieldFunc);
        try {
            Class<?> target = item.getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToOne annotation = declaredField.getAnnotation(OneToOne.class);
            Class<?> targetService = annotation.targetService();
            this.oneToOneSetValue(Arrays.asList(item), annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    default <T> void oneToOne(List<T> itemList, PropertyFunc<T, ?> mapFieldFunc) {
        if (itemList == null || itemList.isEmpty()) return;
        String fieldValue = ObjectUtils.getFieldName(mapFieldFunc);
        try {
            Class<?> target = itemList.get(0).getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToOne annotation = declaredField.getAnnotation(OneToOne.class);
            Class<?> targetService = annotation.targetService();
            this.oneToOneSetValue(itemList, annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default <T> void oneToOneSetValue(List<T> itemList, OneToOne annotation, Class<?> targetService, Field declaredField){
        try {
            Object bean = SpringContextUtils.getBean(targetService);
            String localKey = annotation.localKey();
            String foreignKey = annotation.foreignKey();
            //通过这个declaredField来为列表对象赋值查询到的对象
            declaredField.setAccessible(true);
            Class<?> itemClass = itemList.get(0).getClass();
            Field localField = itemClass.getDeclaredField(StringUtils.underscoreToCamelCase(localKey));
            localField.setAccessible(true);
            Set<Object> ids = new HashSet<>();
            for (T t : itemList) {
                ids.add(localField.get(t));
            }
            QueryWrapper<T> tQueryWrapper = new QueryWrapper();
            tQueryWrapper.in(foreignKey, ids);

            BaseService foreignServiceBean = (BaseService) bean;
            List<T> list = foreignServiceBean.list(tQueryWrapper);
            Map<Object, T> objectTMap = EntityUtils.resortEntityByColumnLevel1(list, foreignKey);

            for (T t : itemList) {
                declaredField.set(t,objectTMap.get(localField.get(t)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
