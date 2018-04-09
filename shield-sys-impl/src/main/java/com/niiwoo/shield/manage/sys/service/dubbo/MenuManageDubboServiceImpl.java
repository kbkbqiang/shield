package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.sys.dto.MenuDTO;
import com.niiwoo.shield.manage.sys.service.MenuManageDubboService;
import com.niiwoo.shield.manage.sys.service.local.MenuLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Slf4j
@Service(version = "1.0.0")
public class MenuManageDubboServiceImpl implements MenuManageDubboService {

    @Autowired
    private MenuLocalService menuLocalService;

    @Override
    public List<MenuDTO> queryMenuTreeList(Map<String, Object> params) {
        return menuLocalService.queryMenuTreeList(params);
    }

    @Override
    public List<MenuDTO> queryParentMenuDownList() {
        return menuLocalService.queryParentMenuDownList();
    }

    @Override
    public void addMenu(MenuDTO dto) {
        menuLocalService.addMenu(dto);
    }

    @Override
    public void updateMenu(MenuDTO dto) {
        menuLocalService.updateMenu(dto);
    }

    @Override
    public MenuDTO findSysMenuById(Long menuId) {
        return menuLocalService.findSysMenuById(menuId);
    }

    @Override
    public void dropMenu(Long menuId) {
        menuLocalService.dropMenu(menuId);
    }
}
