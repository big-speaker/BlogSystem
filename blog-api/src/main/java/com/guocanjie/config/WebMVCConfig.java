package com.guocanjie.config;

import com.guocanjie.handle.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

//    跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

//    添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        设置拦截器拦截所有请求路径，除了/login 和 /register 路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register");
//                .excludePathPatterns("/articles")
//                .excludePathPatterns("/articles/hot")
//                .excludePathPatterns("/articles/new")
//                .excludePathPatterns("/tags/hot")
//                .excludePathPatterns("/categorys/**")
//                .excludePathPatterns("/tags/**")
//                .excludePathPatterns("/articles/archives");
    }
}
