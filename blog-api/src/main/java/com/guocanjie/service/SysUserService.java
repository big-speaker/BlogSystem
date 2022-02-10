package com.guocanjie.service;

import com.guocanjie.dao.pojo.Login;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.LoginUserVo;
import com.guocanjie.utils.vo.UserVo;

public interface SysUserService {

//    通过文章id查询
    public SysUser getSysUser(Long articleId);

//    查询登陆账号密码
    public SysUser checkLogin(Login login);

//    通过token查询用户信息
    public Request getUserData(String token);

//    查询注册账号是否存在
    public SysUser checkRegister(Login login);

//    将注册的账号保存到数据库
    public Boolean insertAccount(SysUser sysUser);

//    根据用户id查询用户信息
    public UserVo getUserVo(Long id);
}
