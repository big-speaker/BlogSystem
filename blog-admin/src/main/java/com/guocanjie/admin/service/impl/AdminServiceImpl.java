package com.guocanjie.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.admin.dao.mapper.AdminMapper;
import com.guocanjie.admin.pojo.Admin;
import com.guocanjie.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public Admin getAdmin(String username) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getUsername,username);
        lambdaQueryWrapper.last("limit 1");
        Admin admin = adminMapper.selectOne(lambdaQueryWrapper);
        return admin;
    }
}
