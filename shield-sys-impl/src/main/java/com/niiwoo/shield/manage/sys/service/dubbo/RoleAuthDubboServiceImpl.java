package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.sys.dto.AuthManageDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.shield.manage.sys.service.RoleAuthDubboService;
import com.niiwoo.shield.manage.sys.service.local.RoleAuthManageLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/20.
 */
@Slf4j
@Service(version = "1.0.0")
public class RoleAuthDubboServiceImpl implements RoleAuthDubboService {
    @Autowired
    private RoleAuthManageLocalService roleAuthManageLocalService;


    @Override
    public void checkRoleId(Long roleId) {
        roleAuthManageLocalService.checkRoleId(roleId);
    }

    @Override
    public void checkMenuId(Long menuId) {
        roleAuthManageLocalService.checkMenuId(menuId);
    }

    @Override
    public Map<String,Object> queryMenusByRoleId(Long roleId) {
        return roleAuthManageLocalService.queryMenusByRoleId(roleId);
    }

    @Override
    public Map<String, Object> queryPermissionsByRoleId(AuthManageDTO dto) {
        return roleAuthManageLocalService.queryPermissionsByRoleId(dto);
    }

    @Override
    public void updateRolePermission(AuthManageDTO dto, String principal) {
        roleAuthManageLocalService.updateRolePermission(dto,principal);
    }

    @Override
    public void updatePermissionsForMenu(Long roleId, Long menuId, Long[] permissionIds, String principal) {
        roleAuthManageLocalService.updatePermissionsForMenu(roleId,menuId,permissionIds,principal);
    }
}
