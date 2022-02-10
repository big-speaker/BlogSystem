package com.guocanjie.aop.cache;

import com.alibaba.fastjson.JSON;
import com.guocanjie.utils.Request;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 定义一个切面 用于缓存接口数据
 */
@Component
@Aspect
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    定义切点
    @Pointcut("@annotation(com.guocanjie.aop.cache.CacheAnnotation)")
    public void pt(){};

//    定义一个切面，绑定切点
    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint){
        try {
//        获取类名
            String className = joinPoint.getTarget().getClass().getName();
//        获取方法名
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();

//        获取请求参数长度
            int paramLength = joinPoint.getArgs().length;
//        创建一个类数组存放 请求参数类
            Class[] parameterTypes = new Class[paramLength];
//        创建一个对象数组存放 请求参数
            Object[] args = joinPoint.getArgs();

//        判断是否有参数，如果有参数，将参数连接成字符串 并存放在参数类中
            String param = "";
            for(int i = 0; i<paramLength; i++){
                if(args[i] != null){
                    param += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                }else{
                    parameterTypes[i] = null;
                }
            }

//        获取该切点的CacheAnnotation注解的参数
            Method method = signature.getDeclaringType().getMethod(methodName, parameterTypes);
            CacheAnnotation annotation = method.getAnnotation(CacheAnnotation.class);
            long expir = annotation.expir();
            String name = annotation.name();

//        将参数字符串加密
            param = DigestUtils.md5Hex(param);
//        用注解名、类名、方法名、加密后的参数组成redis缓存的键
            String redisKey = name + "::" +className + "::" + methodName + "::" + param;

//        先应键从redis查找数据，找到则返回，找不到则写入redis返回
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if(StringUtils.isNotEmpty(redisValue)){
                log.info("走了缓存,{},{}",className,methodName);
                return JSON.parseObject(redisValue,Request.class);
            }else{
    //            执行方法获得返回参数
                Object proceed = joinPoint.proceed();
                redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expir));
                log.info("存入缓存,{},{}",className,methodName);
                return proceed;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new Request("系统错误");
    }

}
