package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.sys.dto.*;
import com.niiwoo.tripod.provider.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@Slf4j
@Service
public class RoleAuthManageLocalService {


    @Autowired
    private RoleLocalService roleLocalService;

    @Autowired
    private MenuLocalService menuService;

    @Autowired
    private RoleMenuLocalService roleMenuService;

    @Autowired
    private RolePermissionLocalService rolePermissionService;


    /**
     * 校验角色ID
     * @param roleId
     * @return
     */
    public void checkRoleId(Long roleId){
        if(roleId==null || roleId==0){
            throw new BizException("MS_100004", "角色ID");
        }
        //角色ID校验
        RoleDTO roleDTO = roleLocalService.findSysRoleById(roleId);
        if(roleDTO == null){
            throw new BizException("MS_100002", "角色");
        }
    }


    /**
     * 校验菜单ID
     * @param menuId
     * @return
     */
    public void checkMenuId(Long menuId){
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004", "菜单ID");
        }
        //角色ID校验
        MenuDTO tmpMenu = menuService.findSysMenuById(menuId);
        if(tmpMenu == null){
            throw new BizException("MS_100002", "菜单");
        }
    }


    /**
     * 查询树状菜单授权数据
     * @param roleId
     * @return
     */
    public Map<String,Object> queryMenusByRoleId(Long roleId){
        //参数校验
        checkRoleId(roleId);
        try {
            //查询角色的菜单列表,树状结构
            Map<String,Object> dataMap = new HashMap<String,Object>();
            List<RoleMenuDataDTO> roleMenuDataList = roleMenuService.queryMenuListByRoleId(roleId);
            dataMap.put("list",roleMenuDataList);
            return dataMap;
        }catch(Exception e){
            log.error("查询树状菜单授权数据操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "查询树状菜单授权数据");
        }
    }


    /**
     * 查询角色的资源权限数据
     * @param dto
     * @return
     */
    public Map<String,Object> queryPermissionsByRoleId(AuthManageDTO dto){
        Long roleId = dto.getRoleId();
        Long menuId = dto.getMenuId();

        //角色ID校验
        checkRoleId(roleId);
        //菜单ID校验
        checkMenuId(menuId);

        Map<String,Object> dataMap = new HashMap<String,Object>();
        try {
            //查询角色的资源权限列表
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("roleId",roleId);
            params.put("menuId",menuId);
            //查询角色的资源授权数据
            List<RolePermissionDataDTO> permissionList = rolePermissionService.queryPermissionList(params);
            dataMap.put("list",permissionList);
            return dataMap;
        }catch(Exception e){
            log.error("查询角色的资源权限数据操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "查询角色的资源权限数据");
        }
    }


    /**
     * 更新角色菜单及资源权限
     * @param dto
     * @param principal
     * @return
     */
    @Transactional
    public void updateRolePermission(AuthManageDTO dto, String principal){
        //角色ID校验
        checkRoleId(dto.getRoleId());

        try {
            Long roleId = dto.getRoleId();
            Long[] menuIds = dto.getMenuIds();
            Long menuId = dto.getMenuId();
            Long[] permissionIds = dto.getPermissionIds();

            if(menuIds.length==0){
                //撤销该角色的所有菜单权限
                roleMenuService.deleteRoleMenusByRoleId(roleId);
                //撤销该角色的所有资源权限
                rolePermissionService.deleteRolePermissionsByRoleId(roleId);
            }else {
                //更新菜单权限
                List<Long> oldMenuIds = roleMenuService.queryMenuIdsByRoleId(roleId);
                List<Long> newMenuIds = Arrays.asList(menuIds);
                for (Long tmpMenuId : newMenuIds) {
                    //若旧菜单列表不包含某新菜单，则添加该菜单权限
                    if (!oldMenuIds.contains(tmpMenuId)) {
                        //添加新的菜单权限
                        roleMenuService.addRoleMenu(roleId, tmpMenuId, principal);
                    }
                }

                for (Long tmpMenuId : oldMenuIds) {
                    //若新菜单列表不包含某旧菜单，则撤销该菜单权限
                    if (!newMenuIds.contains(tmpMenuId)) {
                        //撤销菜单权限
                        roleMenuService.deleteRoleMenu(roleId, tmpMenuId);
                        //同时，撤销该菜单下的所有资源权限
                        List<Long> permIds = rolePermissionService.queryPermissionsByCondition(roleId, tmpMenuId);
                        if (permIds != null && !permIds.isEmpty()) {
                            //批量撤销资源权限
                            rolePermissionService.batchDeleteRolePermission(roleId, permIds);
                        }
                    }
                }

                //更新角色当前菜单的资源权限
                updatePermissionsForMenu(roleId, menuId, permissionIds, principal);
            }
        }catch(Exception e){
            log.error("更新角色菜单及资源权限操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "更新角色菜单及资源权限");
        }

    }


    /**
     * 更新角色某菜单的资源权限
     * @param roleId
     * @param menuId
     * @param permissionIds
     * @param principal
     */
    @Transactional
    public void updatePermissionsForMenu(Long roleId, Long menuId, Long[] permissionIds, String principal){
        if(menuId==null || menuId==0){
            return;
        }

        //更新当前菜单下的资源权限
        List<Long> oldPermissionIds = rolePermissionService.queryPermissionsByCondition(roleId,menuId);
        if(permissionIds.length==0) {
            if(oldPermissionIds!=null && !oldPermissionIds.isEmpty()) {
                //批量撤销资源权限
                rolePermissionService.batchDeleteRolePermission(roleId, oldPermissionIds);
            }
            return;
        }

        List<Long> newPermissionIds = Arrays.asList(permissionIds);
        //若旧资源列表不包含某新资源，则添加该资源权限
        for(Long tmpPermissionId : newPermissionIds){
            if(!oldPermissionIds.contains(tmpPermissionId)){
                //添加新的资源权限
                rolePermissionService.addRolePermission(roleId,tmpPermissionId,principal);
            }
        }
        //若新资源列表不包含某旧资源，则撤销该资源权限
        for(Long tmpPermissionId : oldPermissionIds){
            if(!newPermissionIds.contains(tmpPermissionId)){
                //撤销资源权限
                rolePermissionService.deleteRolePermission(roleId,tmpPermissionId);
            }
        }
    }
}
