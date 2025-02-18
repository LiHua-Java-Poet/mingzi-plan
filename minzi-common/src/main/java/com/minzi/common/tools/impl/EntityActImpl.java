package com.minzi.common.tools.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minzi.common.annotation.OneToOne;
import com.minzi.common.service.BaseService;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class EntityActImpl implements EntityAct {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public <T> void OneToOneSetValue(List<T> itemList, OneToOne annotation, Class<?> targetService, Field declaredField) throws NoSuchFieldException, IllegalAccessException {
        Object bean = applicationContext.getBean(targetService);
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
    }

}
