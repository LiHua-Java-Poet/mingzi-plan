package com.minzi.common.core.tools.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 用于表示前缀的字符串
     * @return
     */
    String prefixKey() default DistributedLockConstant.NONE_KEY;

    /**
     * 锁定的key值，比如具体到某个数据
     * @return
     */
    String key() default DistributedLockConstant.NONE_KEY;

}
