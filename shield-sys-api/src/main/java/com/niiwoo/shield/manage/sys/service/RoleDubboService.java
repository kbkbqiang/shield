package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.QueryRoleListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by luosiwen on 2017/12/20.
 */
public interface RoleDubboService {


    void addRole(RoleDTO dto);

    void grantDefaultPermission(Long roleId, String principal);

    void updateRole(RoleDTO dto);

    Set<Long> dropRole(Long roleId);

    RoleDTO findSysRoleById(Long roleId);

    List<RoleDTO> queryAllRoleList();

    Set<Long> getUserIdsByRoleId(Long roleId);

    Integer queryRoleListCount(Page page);

    Page<RoleDTO> queryRoleList(QueryRoleListDTO dto);

    /**
     * 查询角色集合
     * @param userId
     * @return
     */
    List<RoleDTO> queryRolesByUserId(Long userId);
}
