package com.guocanjie.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

//    创建一个自己用来加密的密钥
    private static final String key = "123456789guocanjie###";

//    创建一个token
    public static String createToken(Long userId){
//        创建一个用来存放登陆用户id的hashmap
        Map<String,Object> userMap = new HashMap<>();
//        创建一个"userId"的列,将userId变量写HashMap的入userId中
        userMap.put("userId",userId);
//        创建一个token
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,key)
                .setClaims(userMap)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000));
        String token = jwtBuilder.compact();
        return token;
    }

//    解析并判断一个token
    public static Map<String,Object> checkToken(String token){
        try {
//        解析token得到HashMap
            Jwt parse = Jwts.parser().setSigningKey(key).parse(token);
            Object body = parse.getBody();
//        返回对应的HashMap
            return (Map<String, Object>)body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    测试token的解密与加密
public static void main(String[] args) {
    Long userId = 201835020309L;
    String token = createToken(userId);
    System.out.println("将用户ID变成Token之后:"+token);
    System.out.println("=============================");
    Map<String, Object> stringObjectMap = checkToken(token);
    System.out.println("将Token解析成用户ID之后:"+stringObjectMap.get("userId"));
}

}
