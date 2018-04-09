package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.AuthManageDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/20.
 */
public interface RoleAuthDubboService {
    /**
     * 校验角色编号
     * @param roleId
     */
    void checkRoleId(Long roleId);


    /**
     * 校验菜单ID
     * @param menuId
     */
    void checkMenuId(Long menuId);

    /**
     * 查询树状菜单授权数据
     * @param roleId
     * @return
     */
    Map<String,Object> queryMenusByRoleId(Long roleId);

    /**
     * 查询角色的资源权限数据
     * @param dto
     * @return
     */
    Map<String,Object> queryPermissionsByRoleId(AuthManageDTO dto);

    /**
     * 更新角色菜单及资源权限
     * @param dto
     * @param principal
     */
    void updateRolePermission(AuthManageDTO dto, String principal);

    /**
     * 更新角色某菜单的资源权限
     * @param roleId
     * @param menuId
     * @param permissionIds
     * @param principal
     */
    void updatePermissionsForMenu(Long roleId, Long menuId, Long[] permissionIds, String principal);
}
