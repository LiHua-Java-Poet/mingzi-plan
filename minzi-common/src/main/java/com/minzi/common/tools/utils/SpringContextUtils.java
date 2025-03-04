package com.minzi.common.tools.utils;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;


@Component
public class SpringContextUtils implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }


    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        return applicationContext.getBeansOfType(type);
    }

    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return applicationContext.getBeanNamesForAnnotation(annotationType);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    public <A extends Annotation> A findAnnotationOnBean(String var1, Class<A> var2) throws NoSuchBeanDefinitionException {
        return applicationContext.findAnnotationOnBean(var1, var2);
    }
}
