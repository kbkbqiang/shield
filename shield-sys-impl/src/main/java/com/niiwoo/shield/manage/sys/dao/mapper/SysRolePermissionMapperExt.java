package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysRolePermission;
import com.niiwoo.shield.manage.sys.dto.RolePermissionDataDTO;
import com.niiwoo.shield.manage.sys.dto.SysRolePermissionDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRolePermissionMapperExt extends SysRolePermissionMapper{

    /**
     * 删除角色的资源权限
     * @param roleId
     */
    void deleteRolePermissionsByRoleId(Long roleId);

    //根据角色ID查询资源权限列表
    List<RolePermissionDataDTO> queryPermissionList(Map<String, Object> params);

    //创建新的角色菜单权限
    void addRolePermission(SysRolePermissionDTO rolePermission);

    //删除角色菜单权限
    void deleteRolePermission(SysRolePermissionDTO rolePermission);

    //批量撤销资源权限
    void batchDeleteRolePermission(Map<String, Object> params);
    /**
     * 根据条件查询角色的资源权限列表
     * @param params
     * @return
     */
    List<Long> getPermissionsByCondition(Map<String, Object> params);

    //查询用户的资源权限
    Set<String> getPermissionsByUserRoles(Map<String, Object> params);

    /**
     * 根据ID查询资源数量
     * @param permissionId
     * @return
     */
    Integer queryRolePermissionCountByPermId(Long permissionId);

    /**
     * 根据资源ID查询拥有该权限的用户列表
     * @param permissionId
     * @return
     */
    Set<Long> queryUserIdListByPermId(Long permissionId);



}