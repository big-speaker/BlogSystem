package com.guocanjie.handle;

import com.alibaba.fastjson.JSON;
import com.guocanjie.dao.pojo.Login;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.service.LoginService;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        打印所有请求日志
        log.info("===============================request start =====================================");
        log.info("request url:{}",request.getRequestURI());
        log.info("requesr method:{}",request.getMethod());
        log.info("token:{}",request.getHeader("Oauth-Token"));
        log.info("===============================request end =====================================");

//        第一步，判断请求方法是否为Controller
//        如果请求的不是Controller，而是resources下的static，则放行
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
//        第二步，判断请求头的token是否为空
        String token = request.getHeader("Oauth-Token");
        if(StringUtils.isEmpty(token)){
            Request request1 = new Request(200, "请先登陆", null);
//            设置响应数据类型为json数据 utf-8编码
            response.setContentType("application/json;charset=utf-8");
//            返回未登录提示
            response.getWriter().print(JSON.toJSONString(request1));
            return false;
        }
//        第三步，如果token不为空，进行token验证
        SysUser sysUser = loginService.getUserData(request.getHeader("Oauth-Token"));
//        如果token已经过期，提示重新登陆
        if(sysUser == null){
            Request request1 = new Request(200, "登陆状态已经过期，请重新登陆", null);
//            设置响应数据类型为json数据 utf-8编码
            response.setContentType("application/json;charset=utf-8");
//            返回未登录提示
            response.getWriter().print(JSON.toJSONString(request1));
            return false;
        }
//        将用户信息放入线程
        UserThreadLocal.put(sysUser);
        return true;
    }

//    所有操作完成后删除本地线程的用户信息
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
