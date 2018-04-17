package com.geeker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gaol
 * @description ${description}
 * @create 2018/2/28
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditCut {
    /***操作平台**/
    int platform();
    /***操作功能名称***/
    String funcName() default "";
}
