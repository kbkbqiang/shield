package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysPermission;
import com.niiwoo.shield.manage.sys.dao.entity.SysRolePermission;
import com.niiwoo.shield.manage.sys.dto.PermissionDTO;

import java.util.List;

public interface SysPermissionMapperExt extends SysPermissionMapper{

    Integer insertSysPermission(SysPermission permission);

    Integer updateSysPermission(SysPermission permission);

    Integer deleteSysPermission(Long permissionId);

    SysPermission findSysPermissionById(Long permissionId);

    Integer findSysPermCountByCondition(PermissionDTO permissionDTO);

    List<PermissionDTO> queryPermissionsByMenuId(Long menuId);

    List<PermissionDTO> findSysPermissionByValue(String permissionValue);

    /**
     * 根据userid查询用户权限列表
     * @param userId
     * @return
     */
    List<String> queryPermissionsByUserid(Long userId);
}