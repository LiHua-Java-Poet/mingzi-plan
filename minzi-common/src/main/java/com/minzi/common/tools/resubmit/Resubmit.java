package com.minzi.common.tools.resubmit;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resubmit {

    /**
     * 用于表示参数类型
     * @return
     */
    Class voClass() default Object.class;
}
