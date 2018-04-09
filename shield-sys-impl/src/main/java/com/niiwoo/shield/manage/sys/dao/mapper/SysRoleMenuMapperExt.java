package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysRoleMenu;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;

import java.util.List;
import java.util.Map;

public interface SysRoleMenuMapperExt extends SysRoleMenuMapper{

    /**
     * 删除角色的菜单权限
     * @param roleId
     */
    void deleteRoleMenusByRoleId(Long roleId);

    //添加角色菜单权限
    void addRoleMenu(RoleMenuDTO roleMenuDTO);

    //删除角色菜单权限
    void deleteRoleMenu(RoleMenuDTO roleMenuDTO);
    //根据菜单列表删除角色的菜单权限
    void deleteRoleMenuByMenuIds(Map<String, Object> params);

    List<Long> queryMenuIdsByRoleId(Long roleId);

    //查询菜单所有顶级节点
    List<RoleMenuDataDTO> queryMenuTreeTopList(Map<String, Object> params);

    //查询菜单子节点
    List<RoleMenuDataDTO> queryMenuTreeChildrenList(Map<String, Object> params);

    //查询菜单所有顶级节点
    List<RoleMenuDataDTO> queryMenuTreeTopListByRoles(Map<String, Object> params);

    //查询菜单子节点
    List<RoleMenuDataDTO> queryMenuTreeChildrenListByRoles(Map<String, Object> params);

}