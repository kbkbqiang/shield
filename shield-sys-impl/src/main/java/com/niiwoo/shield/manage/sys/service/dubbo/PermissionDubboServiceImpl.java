package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.sys.dto.PermissionDTO;
import com.niiwoo.shield.manage.sys.service.PermissionDubboService;
import com.niiwoo.shield.manage.sys.service.local.PermissionLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by luosiwen on 2017/12/22.
 */
@Slf4j
@Service(version = "1.0.0")
public class PermissionDubboServiceImpl implements PermissionDubboService{


    @Autowired
    private PermissionLocalService permissionLocalService;
    @Override
    public void addPermission(PermissionDTO dto) {
        permissionLocalService.addPermission(dto);
    }

    @Override
    public void updatePermission(PermissionDTO dto) {
        permissionLocalService.updatePermission(dto);

    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionLocalService.deletePermission(permissionId);

    }

    @Override
    public List<PermissionDTO> queryPermissionsByMenuId(Long menuId) {
        return permissionLocalService.queryPermissionsByMenuId(menuId);
    }

    @Override
    public Integer findSysPermCountByCondition(PermissionDTO permission) {
        return permissionLocalService.findSysPermCountByCondition(permission);
    }

    /**
     * 查询用户资源列表
     * @param userId
     * @return
     */
    @Override
    public List<String> queryPermissions(Long userId) {
        return permissionLocalService.queryPermissions(userId);
    }
}
