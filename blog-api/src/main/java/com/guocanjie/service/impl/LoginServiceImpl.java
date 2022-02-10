package com.guocanjie.service.impl;

import com.alibaba.fastjson.JSON;
import com.guocanjie.dao.pojo.Login;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.service.LoginService;
import com.guocanjie.service.SysUserService;
import com.guocanjie.utils.JWTUtils;
import com.guocanjie.utils.Request;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final String salt = "guocanjie##";

    @Autowired
    private SysUserService sysUserService;

//    导入一个redis模板,键值对为  字符串:字符串
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Request login(Login login) {

        String account = login.getAccount();
        String password = login.getPassword();

//        第一步判断用户名密码是否合法
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return new Request(200,"用户名或者密码不能为空", null);
        }
//        将得到的密码加密一下再放回对象
        String newPassword = DigestUtils.md5Hex(password+salt);
        login.setPassword(newPassword);

//        第二步从数据库查询用户名密码是否正确
        SysUser sysUser = sysUserService.checkLogin(login);
        if(sysUser == null){
            return new Request(200,"用户名或密码错误",null);
        }
//        第三步,登陆成功,生成token
        String token = JWTUtils.createToken(sysUser.getId());

//        第四步,将token 和 SysUser 以字符串键值对的形式 缓存到redis模板,设计过期时间为1天
        redisTemplate.opsForValue().set(token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

//        第五步,将token返回
        return new Request(token);
    }

    @Override
    public SysUser getUserData(String token) {
//        第一步，判断token是否为空
        if(StringUtils.isEmpty(token)){
            return null;
        }
//        第二步，判断token是否能解析
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
//        第三步，判断token是否在redis内（即判断token是否过期）
        String s = redisTemplate.opsForValue().get(token);
        if(StringUtils.isEmpty(s)){
            return null;
        }
//        第四步，将redis上存储的用户信息字符串重新写入SysUser对象，返回SusUser对象
        SysUser sysUser = JSON.parseObject(s, SysUser.class);
//        SysUser sysUser = new SysUser();
//        sysUser.setAccount("郭灿杰");
        return sysUser;
    }

    @Override
    public Request logout(String token) {
        redisTemplate.delete(token);
        return new Request(200,"退出登陆成功",null);
    }

    @Override
    public Request register(Login login) {
//        第一步，判断账号密码名称是否为空
        if(StringUtils.isEmpty(login.getAccount()) || StringUtils.isEmpty(login.getPassword()) || StringUtils.isEmpty(login.getNickname())){
            return new Request(200, "账号或密码或用户名不能为空", null);
        }
//        第二步，判断账号是否已经注册
        if(sysUserService.checkRegister(login) != null){
            return new Request(200, "该账号已注册，请直接登录", null);
        }
//        第三步，账号未被注册，直接进行注册
        SysUser sysUser = new SysUser();
        sysUser.setAccount(login.getAccount());
            sysUser.setPassword(DigestUtils.md5Hex(login.getPassword()+salt));
        sysUser.setNickname(login.getNickname());
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setAvatar("http://localhost:8080/static/img/logo.b3a48c0.png");
        sysUser.setEmail("");
        sysUser.setMobilePhoneNumber("");
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUserService.insertAccount(sysUser);

//        第四步,登陆生成token
        String token = JWTUtils.createToken(sysUser.getId());

//        第五步,将token 和 SysUser 以字符串键值对的形式 缓存到redis模板,设计过期时间为1天
        redisTemplate.opsForValue().set(token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

//        第六步,将token返回
        return new Request(token);
    }


}
