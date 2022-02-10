package com.guocanjie.admin.service;


import com.guocanjie.admin.pojo.Admin;

public interface AdminService {

//    通过username查询admin账户信息
    public Admin getAdmin(String username);
}
