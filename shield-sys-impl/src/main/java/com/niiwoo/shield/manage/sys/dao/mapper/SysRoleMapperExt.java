package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysRole;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;

import java.util.List;
import java.util.Set;

public interface SysRoleMapperExt extends SysRoleMapper{

    RoleDTO querySysRoleByName(String roleName);

    /**
     * 根据角色id查询用户id列表
     * @param roleId
     * @return
     */
    Set<Long> getUserIdsByRoleId(Long roleId);


    RoleDTO findSysRoleById(Long roleId);

    List<RoleDTO> queryAllRoleList();

    /**
     * 分页查询角色总记录数
     * @param page
     * @return
     */
    Integer queryRoleListCount(Page page);

    /**
     * 分页查询角色列表
     * @param page
     * @return
     */
    List<RoleDTO> queryRoleList(Page<RoleDTO> page);


    /**
     * 通过userId查询角色列表
     * @param userId
     * @return
     */
    List<SysRole> queryRolesByUserId(Long userId);
}