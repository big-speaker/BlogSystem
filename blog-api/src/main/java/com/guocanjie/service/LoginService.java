package com.guocanjie.service;

import com.guocanjie.dao.pojo.Login;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.utils.Request;

import java.util.StringTokenizer;

public interface LoginService {

//    登陆验证
    public Request login(Login login);

//    检查token，返回用户信息
    public SysUser getUserData(String token);

//    退出登陆
    public Request logout(String token);

//    注册功能
    public Request register(Login login);
}
