package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysUserRole;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapperExt extends SysUserRoleMapper{

    /**
     * 删除用户角色关联关系
     * @param userRole
     */
    void deleteSysUserRole(SysUserRole userRole);


    /**
     * 根据userId 查询用户角色关联信息
     * @param userId
     * @return
     */
    List<SysUserRole> getUserRolesByUserId(Long userId);


    /**
     * 获取用户角色
     * @param userRole
     * @return
     */
    SysUserRole getUserRoleByParam(SysUserRole userRole);


    /**
     * 根据userName查询角色ids
     * @param userName
     * @return
     */
    List<SysUserRole> getUserRolesByUserName(String userName);

    /**
     * 根据用户id及角色名称获取用户角色
     *
     * @param userId   用户id
     * @param roleName 角色名称
     * @return 角色信息
     */
    RoleDTO getUserRoleByRoleName(@Param("userId") Long userId, @Param("roleName") String roleName);
}