package com.minzi.common.core.tools.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口缓存更新
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheClean {

    /**
     * 业务类型字符串
     */
    String business();

    /**
     * 是否区分角色，该接口的缓存键是否会拼接用户id,这里默认要拿到用户的id
     */
    boolean userId() default true;
}
