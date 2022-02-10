package com.guocanjie.controller;

import com.guocanjie.service.SysUserService;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Request getCurrentUser(@RequestHeader("Authorization") String token){
        return sysUserService.getUserData(token);
    }
}
