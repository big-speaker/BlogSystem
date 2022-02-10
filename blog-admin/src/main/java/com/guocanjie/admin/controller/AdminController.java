package com.guocanjie.admin.controller;

import com.guocanjie.admin.pojo.Permission;
import com.guocanjie.admin.service.PermissionService;
import com.guocanjie.admin.vo.PageParam;
import com.guocanjie.admin.vo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("permission/permissionList")
    public Request getPageList(@RequestBody PageParam pageParam){
        return permissionService.getPageList(pageParam);
    }

    @PostMapping("permission/add")
    public Request add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Request updata(@RequestBody Permission permission){
        return permissionService.updata(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Request delete(@PathVariable Long id){
        return permissionService.remove(id);
    }
}
