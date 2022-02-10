package com.guocanjie.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guocanjie.admin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT * FROM ms_permission WHERE id IN (SELECT permission_id FROM ms_admin_permission WHERE admin_id = #{id})")
    public List<Permission> getPermissionByAdminId(Long id);
}
