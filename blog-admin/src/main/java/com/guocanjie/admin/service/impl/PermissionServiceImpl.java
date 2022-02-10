package com.guocanjie.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guocanjie.admin.dao.mapper.PermissionMapper;
import com.guocanjie.admin.pojo.Permission;
import com.guocanjie.admin.service.PermissionService;
import com.guocanjie.admin.vo.PageParam;
import com.guocanjie.admin.vo.PageResult;
import com.guocanjie.admin.vo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Request getPageList(PageParam pageParam) {
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(pageParam.getQueryString() != null){
            lambdaQueryWrapper.eq(Permission::getName, pageParam.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page, lambdaQueryWrapper);
        List<Permission> records = permissionPage.getRecords();
        long total = permissionPage.getTotal();

        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(records);
        pageResult.setTotal(total);
        return new Request(pageResult);
    }

    @Override
    public Request add(Permission permission) {
        int insert = permissionMapper.insert(permission);
        return new Request(insert);
    }

    @Override
    public Request updata(Permission permission) {
        int update = permissionMapper.updateById(permission);
        return new Request(update);
    }

    @Override
    public Request remove(Long id) {
        int delete = permissionMapper.deleteById(id);
        return new Request(delete);
    }

    @Override
    public List<Permission> getPermissionByAdminId(Long id) {
        return permissionMapper.getPermissionByAdminId(id);
    }
}
