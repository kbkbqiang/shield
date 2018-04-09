package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.QueryRoleListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.service.RoleDubboService;
import com.niiwoo.shield.manage.sys.service.local.RoleLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * Created by luosiwen on 2017/12/20.
 */
@Slf4j
@Service(version = "1.0.0")
public class RoleDubboSericeImpl implements RoleDubboService {

    @Autowired
    private RoleLocalService roleLocalService;

    @Override
    public void addRole(RoleDTO dto) {
        roleLocalService.addRole(dto);
    }

    @Override
    public void grantDefaultPermission(Long roleId, String principal) {
        roleLocalService.grantDefaultPermission(roleId,principal);
    }

    @Override
    public void updateRole(RoleDTO dto) {
        roleLocalService.updateRole(dto);
    }

    @Override
    public Set<Long> dropRole(Long roleId) {
        return roleLocalService.dropRole(roleId);
    }

    @Override
    public RoleDTO findSysRoleById(Long roleId) {
        return roleLocalService.findSysRoleById(roleId);
    }

    @Override
    public List<RoleDTO> queryAllRoleList() {
        return roleLocalService.queryAllRoleList();
    }

    @Override
    public Set<Long> getUserIdsByRoleId(Long roleId) {
        return roleLocalService.getUserIdsByRoleId(roleId);
    }

    @Override
    public Integer queryRoleListCount(Page page) {
        return roleLocalService.queryRoleListCount(page);
    }

    @Override
    public Page<RoleDTO> queryRoleList(QueryRoleListDTO dto) {
        return roleLocalService.queryRoleList(dto);
    }


    /**
     * 查询角色集合
     * @param userId
     * @return
     */
    @Override
    public List<RoleDTO> queryRolesByUserId(Long userId) {
        return roleLocalService.queryRolesByUserId(userId);
    }
}
