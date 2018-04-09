package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysMenu;
import com.niiwoo.shield.manage.sys.dto.MenuDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysMenuMapperExt extends SysMenuMapper{

    Integer insertSysMenu(SysMenu menu);

    Integer updateSysMenu(SysMenu menu);

    MenuDTO findSysMenuById(Long menuId);

    Integer selectCountByUrl(String url);

    Long findParentIdByMenuId(Long menuId);

    Set<Long> findChildrenByMenuId(Long menuId);

    List<MenuDTO> queryMenuTreeTopList(Map<String, Object> params);

    List<MenuDTO> queryMenuTreeChildrenList(Map<String, Object> params);

    void dropMenuByMenuIds(Map<String, Object> params);
}