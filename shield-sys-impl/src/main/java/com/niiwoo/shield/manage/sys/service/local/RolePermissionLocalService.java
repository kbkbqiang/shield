package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.sys.dao.entity.SysRolePermission;
import com.niiwoo.shield.manage.sys.dao.mapper.SysRolePermissionMapper;
import com.niiwoo.shield.manage.sys.dao.mapper.SysRolePermissionMapperExt;
import com.niiwoo.shield.manage.sys.dto.RolePermissionDataDTO;
import com.niiwoo.shield.manage.sys.dto.SysRolePermissionDTO;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Service
public class RolePermissionLocalService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private SysRolePermissionMapperExt sysRolePermissionMapper;
    @Autowired
    private UserRoleLocalService userRoleLocalService;

    /**
     * 添加角色资源权限
     * @param roleId
     * @param permissionId
     * @param principal
     */
    @Transactional
    public void addRolePermission(Long roleId,long permissionId,String principal){
        SysRolePermissionDTO rolePermission = new SysRolePermissionDTO();
        Long id = snowflakeIdWorker.nextId();
        rolePermission.setId(id);
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermission.setCreatedUser(principal);
        rolePermission.setUpdatedUser(principal);
        sysRolePermissionMapper.addRolePermission(rolePermission);
    }

    /**
     * 删除角色的资源权限
     * @param roleId
     * @param permissionId
     */
    @Transactional
    public void deleteRolePermission(Long roleId,Long permissionId){
        SysRolePermissionDTO rolePermission = new SysRolePermissionDTO();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        sysRolePermissionMapper.deleteRolePermission(rolePermission);
    }

    /**
     * 根据角色ID删除资源权限数据
     * @param roleId
     */
    @Transactional
    public void deleteRolePermissionsByRoleId(Long roleId){
        sysRolePermissionMapper.deleteRolePermissionsByRoleId(roleId);
    }

    /**
     * 批量撤销角色资源权限
     * @param roleId
     * @param permissionIds
     */
    @Transactional
    public void batchDeleteRolePermission(Long roleId,List<Long> permissionIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        params.put("permissionIds",permissionIds);
        sysRolePermissionMapper.batchDeleteRolePermission(params);
    }

    /**
     * 查询角色的资源权限数据
     * @param params
     */
    public List<RolePermissionDataDTO> queryPermissionList(Map<String,Object> params){
        return sysRolePermissionMapper.queryPermissionList(params);
    }

    public List<Long> queryPermissionsByCondition(Long roleId,Long menuId){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        params.put("menuId",menuId);
        return sysRolePermissionMapper.getPermissionsByCondition(params);
    }


    /**
     * 根据用户名称的【资源权限】数据
     * @param userName
     * @return
     */
    public Set<String> getPermissionsByUserName(String userName){
        Set<Long> roleIds = userRoleLocalService.queryRoleIdsByUserName(userName);
        if(roleIds==null || roleIds.isEmpty()){
            return new HashSet<String>();
        }
        //查询角色的资源权限数据
        Set<String> permissions = getPermissionsByRoles(roleIds);
        return permissions;
    }


    /**
     * 根据角色集合查询【资源权限】列表
     * @param roleIds
     * @return
     */
    public Set<String> getPermissionsByRoles(Set<Long> roleIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleIds",roleIds);
        //查询角色的资源权限数据
        Set<String> permissions = sysRolePermissionMapper.getPermissionsByUserRoles(params);
        if(permissions==null){
            return new HashSet<String>();
        }
        return permissions;
    }
}
