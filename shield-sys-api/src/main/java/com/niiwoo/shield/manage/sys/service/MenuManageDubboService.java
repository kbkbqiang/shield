package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.MenuDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/19.
 */
public interface MenuManageDubboService {


    public List<MenuDTO> queryMenuTreeList(Map<String,Object> params);

    List<MenuDTO> queryParentMenuDownList();

    void addMenu(MenuDTO dto);

    void updateMenu(MenuDTO dto);

    MenuDTO findSysMenuById(Long menuId);

    void dropMenu(Long menuId);
}
