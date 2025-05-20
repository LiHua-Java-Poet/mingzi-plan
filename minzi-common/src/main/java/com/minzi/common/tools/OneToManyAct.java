package com.minzi.common.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minzi.common.annotation.OneToMany;
import com.minzi.common.service.BaseService;
import com.minzi.common.tools.utils.SpringContextUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.common.utils.PropertyFunc;
import com.minzi.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public interface OneToManyAct {

    default <T> void oneToMany(T item, PropertyFunc<T, ?> mapFieldFunc) {
        if (item == null) return;
        String fieldValue = ObjectUtils.getFieldName(mapFieldFunc);
        try {
            Class<?> target = item.getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToMany annotation = declaredField.getAnnotation(OneToMany.class);
            Class<?> targetService = annotation.targetService();
            this.oneToManySetValue(Collections.singletonList(item), annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    default <T> void oneToMany(List<T> itemList, PropertyFunc<T, ?> mapFieldFunc) {
        if (itemList == null || itemList.isEmpty()) return;
        String fieldValue = ObjectUtils.getFieldName(mapFieldFunc);
        try {
            Class<?> target = itemList.get(0).getClass();
            Field declaredField = target.getDeclaredField(fieldValue);
            OneToMany annotation = declaredField.getAnnotation(OneToMany.class);
            Class<?> targetService = annotation.targetService();
            this.oneToManySetValue(itemList, annotation, targetService, declaredField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default <T> void oneToManySetValue(List<T> itemList, OneToMany annotation, Class<?> targetService, Field targetEntity) {
        try {
            Object beanService = SpringContextUtils.getBean(targetService);
            String localKey = annotation.localKey();
            String foreignKey = annotation.foreignKey();
            //通过这个declaredField来为列表对象赋值查询到的对象
            Class<?> itemClass = itemList.get(0).getClass();
            Field localField = itemClass.getDeclaredField(StringUtils.underscoreToCamelCase(localKey));
            localField.setAccessible(true);
            targetEntity.setAccessible(true);

            Set<Object> ids = new HashSet<>();
            for (T t : itemList) {
                ids.add(localField.get(t));
            }

            QueryWrapper<T> tQueryWrapper = new QueryWrapper();
            tQueryWrapper.in(foreignKey, ids);

            BaseService foreignServiceBean = (BaseService) beanService;
            List<T> targetEntityList = foreignServiceBean.list(tQueryWrapper);
            Map<Object, List<T>> targetTMap = EntityUtils.resortEntityByColumnLevel2(targetEntityList, StringUtils.underscoreToCamelCase(foreignKey));

            for (T t : itemList) {
                targetEntity.set(t, targetTMap.get(localField.get(t)) == null ? new ArrayList<T>() : targetTMap.get(localField.get(t)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
