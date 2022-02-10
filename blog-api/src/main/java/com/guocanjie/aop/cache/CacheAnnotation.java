package com.guocanjie.aop.cache;


import java.lang.annotation.*;

/**
 * 定义一个自定义注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAnnotation {

//    定义到期时间 默认值为1分钟    定义名字 默认为空
    long expir() default 1 * 60 * 1000;
    String name() default "";
}
