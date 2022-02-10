package com.guocanjie.admin.service;

import com.guocanjie.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        通过用户名查询用户信息admin
        Admin admin = adminService.getAdmin(username);
        if(admin == null){
//            如果查询不到,登陆失败
            return null;
        }
//        如果查询到了用户信息,将用户名密码传入UserDetails并返回
        UserDetails userDetails = new User(username, admin.getPassword(), new ArrayList<>());
        return userDetails;
    }
}
