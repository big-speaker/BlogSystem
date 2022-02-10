package com.guocanjie.utils;

import com.guocanjie.dao.pojo.SysUser;
import lombok.Data;

public class UserThreadLocal {

//    将构造方法私有化
    private UserThreadLocal(){};

//    创建一个线程
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

//    放置用户信息
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

//    获得用户信息
    public static SysUser get(){
        return LOCAL.get();
    }

//    删除用户信息
    public static void remove(){
        LOCAL.remove();
    }
}
