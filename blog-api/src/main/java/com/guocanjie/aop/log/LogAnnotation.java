package com.guocanjie.aop.log;

import java.lang.annotation.*;

/**
 * 创建一个需要打印日志的自定义springboot注解 @LogAnnotation
 */

// ElementType.TYPE表示该注解可用于类上  ElementType.METHOD表示该注解可用于方法上
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

//    为注解设置两个注解参数,两个参数的默认值都为空
    String module() default "";
    String operation() default "";
}
