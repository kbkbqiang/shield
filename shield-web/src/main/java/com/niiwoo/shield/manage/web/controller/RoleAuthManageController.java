package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.sys.dto.AuthManageDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.service.RoleAuthDubboService;
import com.niiwoo.shield.manage.sys.service.RoleDubboService;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.tripod.web.shiro.AuthCacheCleanHandler;
import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by luosiwen on 2017/12/20.
 */
@Slf4j
@RestController
@RequestMapping("/authManage")
@Api(tags = "角色授权管理", description = "/authManage", position = 4)
public class RoleAuthManageController extends BaseController{

    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;
    @Reference(version = "1.0.0")
    private RoleAuthDubboService roleAuthDubboService;
    @Reference(version = "1.0.0")
    private RoleDubboService roleService;
    @Autowired
    private AuthCacheCleanHandler authCacheCleanHandler;


    @ApiOperation("查询角色的菜单列表")
    @PostMapping("/queryMenusByRoleId")
    @RequiresPermissions({"role:queryMenus"})
    public Result<Map<String, Object>> queryMenusByRoleId(@RequestBody AuthManageDTO dto){
        Map<String,Object> dataMap = roleAuthDubboService.queryMenusByRoleId(dto.getRoleId());
        return Result.with(dataMap);
    }



    @ApiOperation("查询角色的资源权限列表")
    @PostMapping("/queryPermissionsByRoleId")
    @RequiresPermissions({"role:queryPermissions"})
    public Result<Map<String,Object>> queryPermissionsByRoleId(@RequestBody AuthManageDTO dto){
        Map<String,Object> rMap= roleAuthDubboService.queryPermissionsByRoleId(dto);
        return Result.with(rMap);
    }


    @ApiOperation("编辑角色权限")
    @PostMapping("/updateRolePermission")
    @RequiresPermissions({"role:grantPermission"})
    public Empty editRolePermission(@RequestBody AuthManageDTO dto){
        UserAuthPrincipal userAuthPrincipal = getCurrentUser();
        roleAuthDubboService.updateRolePermission(dto,userAuthPrincipal.getPrincipal());
        //根据角色ID清除用户权限缓存
        cleanUserPermissionCacheByRole(dto.getRoleId());

        return Empty.getInstance();
    }

    /**
     * 根据角色ID清除用户权限缓存
     * @param roleId
     */
    public void cleanUserPermissionCacheByRole(Long roleId){
        Set<Long> userIds = roleService.getUserIdsByRoleId(roleId);
        List<Long> userIdList = new ArrayList<>();
        userIdList.addAll(userIds);
        List<UserDTO> userDTOs = userDubboService.queryUsersByuserIds(userIdList);

        for (UserDTO userDTO : userDTOs) {
            authCacheCleanHandler.cleanAuthorizationCache(userDTO.getUserName());
        }
    }
}
