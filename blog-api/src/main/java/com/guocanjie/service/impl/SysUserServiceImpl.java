package com.guocanjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.SysUserMapper;
import com.guocanjie.dao.pojo.Login;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.service.LoginService;
import com.guocanjie.service.SysUserService;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.LoginUserVo;
import com.guocanjie.utils.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    LoginService loginService;

    @Override
    public SysUser getSysUser(Long articleId) {
        SysUser sysUser = sysUserMapper.selectById(articleId);;
        return sysUser;
    }

    @Override
    public SysUser checkLogin(Login login) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        查询用户名
        lambdaQueryWrapper.eq(SysUser::getAccount,login.getAccount());
//        查询密码
        lambdaQueryWrapper.eq(SysUser::getPassword,login.getPassword());
//        查询到的对象,抽取账号 用户ID 作者 作品名 封装到SysUser对象
        lambdaQueryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
//        查询到一个账号返回
        lambdaQueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public Request getUserData(String token) {
        SysUser userData = loginService.getUserData(token);
        return new Request(copy(userData));
    }

    @Override
    public SysUser checkRegister(Login login) {
//        新建一个查询条件
        LambdaQueryWrapper<SysUser> loginLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        查询sysuser表中是否有account账号与注册账号相同
        loginLambdaQueryWrapper.eq(SysUser::getAccount,login.getAccount());
//        查询一条数据
        loginLambdaQueryWrapper.last("limit 1");
//        将查询到的结果返回
        return sysUserMapper.selectOne(loginLambdaQueryWrapper);
    }

    @Override
    public Boolean insertAccount(SysUser sysUser) {
        return sysUserMapper.insert(sysUser) > 0;
    }

    @Override
    public UserVo getUserVo(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    public LoginUserVo copy(SysUser sysUser){
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser,loginUserVo);
        return loginUserVo;
    }
}
