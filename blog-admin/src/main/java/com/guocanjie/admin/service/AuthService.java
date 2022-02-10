package com.guocanjie.admin.service;

import com.guocanjie.admin.pojo.Admin;
import com.guocanjie.admin.pojo.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PermissionService permissionService;

    public Boolean auto(HttpServletRequest request, Authentication authentication){
//        获取请求路径
        String requestURI = request.getRequestURI();
        log.info("权限验证request url:{}",requestURI);
//        获取请求用户信息userDetails
        Object principal = authentication.getPrincipal();
        if(principal == null || authentication.equals(principal)){
//            说明未登录 进行拦截
            return false;
        }
//        将principal强转为userDetails获取username
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
//        通过username查询管理员用户admin
        Admin admin = adminService.getAdmin(username);
//        如果用户为空,说明不是管理员用户
        if(admin == null){
            return false;
        }
//        如果用户不为空,查询该管理员用户的权限
        if(admin.getId() == 1){
//            管理员用户为1直接方向
            return true;
        }
        List<Permission> permissionList = permissionService.getPermissionByAdminId(admin.getId());
//        将查询到的权限路径与请求路径进行匹配,成功则放行
        requestURI = StringUtils.split(requestURI,"?")[0];
        for (Permission permission : permissionList) {
            if(requestURI.equals(permission.getPath())){
                log.info("权限通过");
                return true;
            }
        }
        return false;
    }
}
