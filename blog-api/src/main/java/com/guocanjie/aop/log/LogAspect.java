package com.guocanjie.aop.log;

import com.alibaba.fastjson.JSON;
import com.guocanjie.aop.log.LogAnnotation;
import com.guocanjie.utils.HttpContextUtils;
import com.guocanjie.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 *  定义一个切面
 *  切点为标注@LogAnnotation注解的方法
 *  通知关系为：为这个方法打印日志和测试速度
 */
@Component
@Aspect  // 切面注解，定义该类为切面类，类中包含切点和通知
@Slf4j   //日志打印注解
public class LogAspect {

//    定义切点： 切点为标注了@LogAnnotation注解的地方
    @Pointcut("@annotation(com.guocanjie.aop.log.LogAnnotation)")
    public void pt(){};

//    定义通知关系,该通知关系对象切点为pt() 即标注了@LogAnnotation的方法
    @Around("pt()")
//    其中joinPoint，表示切点方法，即标注了@LogAnnotation的方法
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
//        方法执行前 记录时间
        long beginTime = System.currentTimeMillis();
//        执行切点方法
        Object result = joinPoint.proceed();
//        方法执行完毕  记录方法执行时间t
        long time = System.currentTimeMillis() - beginTime;
//        方法执行完毕 记录该方法的执行日志
        recordLog(joinPoint,time);
//        返回执行方法的结果
        return result;
    }

    public void recordLog(ProceedingJoinPoint joinPoint, long time){
        //获取signature的接口方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);  //获取该接口方法上的LogAnnotation注解
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module());  //打印切点上LogAnnotation注解的模块名
        log.info("operation:{}",logAnnotation.operation());  //打印切点上LogAnnotation注解的操作方法

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName(); //获取请求的 接口类名
        String methodName = signature.getName();   // 获取请求的   接口方法名
        log.info("request method:{}",className + "." + methodName + "()");  //打印请求的方法名

//        //请求的参数
        Object[] args = joinPoint.getArgs();    // 获取请求的参数
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);  //打印请求的参数

        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));  // 打印请求的IP地址


        log.info("excute time : {} ms",time);   //打印请求该接口的时间
        log.info("=====================log end================================");
    }

}
