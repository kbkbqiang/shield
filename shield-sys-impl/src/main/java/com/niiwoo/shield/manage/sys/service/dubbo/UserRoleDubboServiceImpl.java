package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.service.UserRoleDubboService;
import com.niiwoo.shield.manage.sys.service.local.RoleLocalService;
import com.niiwoo.shield.manage.sys.service.local.UserRoleLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 用户角色服务
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Slf4j
@Service(version = "1.0.0")
public class UserRoleDubboServiceImpl implements UserRoleDubboService {

    @Autowired
    private RoleLocalService roleLocalService;
    @Autowired
    private UserRoleLocalService userRoleLocalService;

    /**
     * 添加用户角色关联关系
     * @param userId
     * @param roleIds
     * @param principal
     * @param enableFlag
     */
    @Override
    public void addUserRoles(Long userId, Long[] roleIds, String principal, Boolean enableFlag) {
        userRoleLocalService.addUserRoles(userId, roleIds, principal, enableFlag);
    }



    @Override
    public RoleDTO findSysRoleById(Long roleId) {
        return roleLocalService.findSysRoleById(roleId);
    }


    /**
     * 更新用户角色关联信息
     * @param userId
     * @param roleIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    @Override
    public void updateUserRoles(Long userId, Long[] roleIds, String principal, Boolean oldEnableFlag, Boolean newEnableFlag) {
        userRoleLocalService.updateUserRoles(userId, roleIds, principal, oldEnableFlag, newEnableFlag);
    }


    /**
     * 删除用户角色权限
     * @param userIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    @Override
    public void clearUserRoles(Long[] userIds, String principal, Boolean oldEnableFlag,Boolean newEnableFlag) {
        userRoleLocalService.clearUserRoles(userIds, principal, oldEnableFlag, newEnableFlag);
    }

    @Override
    public RoleDTO getUserRoleByName(Long userId, String roleName) {
        Assert.notNull(userId, "管理员id不能为空");
        Assert.notNull(roleName, "角色名称不能为空");
        return userRoleLocalService.getUserRoleByName(userId, roleName);
    }
}
