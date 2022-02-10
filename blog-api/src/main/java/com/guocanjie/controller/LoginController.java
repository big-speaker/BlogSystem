package com.guocanjie.controller;

import com.guocanjie.dao.pojo.Login;
import com.guocanjie.service.LoginService;
import com.guocanjie.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Request login(@RequestBody Login login){
        return loginService.login(login);
    }
}
