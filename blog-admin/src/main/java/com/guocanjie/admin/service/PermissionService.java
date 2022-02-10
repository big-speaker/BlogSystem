package com.guocanjie.admin.service;

import com.guocanjie.admin.pojo.Permission;
import com.guocanjie.admin.vo.PageParam;
import com.guocanjie.admin.vo.Request;

import java.util.List;

public interface PermissionService {

//    分页查询Permission
    public Request getPageList(PageParam pageParam);

//    添加权限
    public Request add(Permission permission);

//    修改权限
    public Request updata(Permission permission);

//    删除权限
    public Request remove(Long id);

//    通过adminId查询权限
    public List<Permission> getPermissionByAdminId(Long id);
}
