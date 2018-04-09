package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysPermission;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long permissionId);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);
}