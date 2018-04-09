package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.PermissionDTO;

import java.util.List;

/**
 * Created by luosiwen on 2017/12/22.
 */
public interface PermissionDubboService {
    /**
     * 添加权限
     * @param dto
     */
    void addPermission(PermissionDTO dto);

    /**
     * 更新权限
     * @param dto
     */
    void updatePermission(PermissionDTO dto);

    /**
     * 删除权限
     * @param permissionId
     */
    void deletePermission(Long permissionId);

    List<PermissionDTO> queryPermissionsByMenuId(Long menuId);

    Integer findSysPermCountByCondition(PermissionDTO permission);

    /**
     * 查询用户资源列表
     * @param userId
     * @return
     */
    List<String> queryPermissions(Long userId);
}
