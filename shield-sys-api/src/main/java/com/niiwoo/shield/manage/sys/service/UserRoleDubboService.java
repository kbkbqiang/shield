package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.RoleDTO;

/**
 * 用户角色
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
public interface UserRoleDubboService {

    /**
     * 添加用户角色关联关系
     *
     * @param userId
     * @param roleIds
     * @param principal
     * @param enableFlag
     */
    void addUserRoles(Long userId, Long[] roleIds, String principal, Boolean enableFlag);


    /**
     * 通过id查询角色
     *
     * @param roleId
     * @return
     */
    RoleDTO findSysRoleById(Long roleId);

    /**
     * 更新用户角色关联
     *
     * @param userId
     * @param roleIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    void updateUserRoles(Long userId, Long[] roleIds, String principal, Boolean oldEnableFlag, Boolean newEnableFlag);


    /**
     * 删除用户角色权限
     *
     * @param userIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    void clearUserRoles(Long[] userIds, String principal, Boolean oldEnableFlag, Boolean newEnableFlag);

    /**
     * 根据用户id及角色名称获取用户角色
     *
     * @param userId   用户id
     * @param roleName 角色名称
     * @return 角色信息
     */
    RoleDTO getUserRoleByName(Long userId,String roleName);
}
