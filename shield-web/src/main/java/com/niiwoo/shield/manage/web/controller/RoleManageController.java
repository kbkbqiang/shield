package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.QueryRoleListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.service.RoleDubboService;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.shiro.AuthCacheCleanHandler;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by luosiwen on 2017/12/20.
 */
@Slf4j
@RestController
@RequestMapping("/roles")
@Api(tags = "角色管理", description = "/roles", position = 3)
public class RoleManageController extends BaseController{

    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;
    @Reference(version = "1.0.0")
    private RoleDubboService roleService;
    @Autowired
    private AuthCacheCleanHandler authCacheCleanHandler;



    @ApiOperation("创建新角色")
    @PostMapping("/addRole")
    @RequiresPermissions({"role:add"})
    public Empty addRole(@RequestBody RoleDTO dto){
        //参数校验
        checkRoleName(dto.getRoleName());
        dto.setCreatedUser(getCurrentUser().getPrincipal());
        dto.setUpdatedUser(getCurrentUser().getPrincipal());
        roleService.addRole(dto);
        return Empty.getInstance();
    }


    @ApiOperation("编辑角色信息")
    @PostMapping("/editRole")
    @RequiresPermissions({"role:edit"})
    public Empty editRole(@RequestBody RoleDTO dto){
        Long roleId = dto.getRoleId();
        //参数校验
        checkRoleId(roleId);
        dto.setUpdatedUser(getCurrentUser().getPrincipal());
        roleService.updateRole(dto);
        return Empty.getInstance();
    }


    @ApiOperation("删除角色信息")
    @PostMapping("/dropRole")
    @RequiresPermissions({"role:drop"})
    public Empty dropRole(@RequestBody RoleDTO dto){
        Long roleId = dto.getRoleId();
        //参数校验
        checkRoleId(roleId);
        dto.setUpdatedUser(getCurrentUser().getPrincipal());
        roleService.dropRole(roleId);
        //根据角色ID清除用户权限缓存
        cleanUserPermissionCacheByRole(roleId);
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


    @ApiOperation("查询角色详情")
    @PostMapping("/queryRoleDetail")
    @RequiresPermissions({"role:queryDetail"})
    public Result<Map<String,Object>> queryRoleDetail(@RequestBody RoleDTO dto){
        Long roleId = dto.getRoleId();
        //参数校验
        checkRoleId(roleId);
        try {
            //查询角色信息
            RoleDTO role = roleService.findSysRoleById(roleId);

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("role",role);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }


    @ApiOperation("分页查询角色信息列表，支持角色名称模糊匹配")
    @PostMapping("/queryRoleList")
    @RequiresPermissions({"role:query"})
    public Result<Page<RoleDTO>> queryRoleList(@RequestBody QueryRoleListDTO dto){
        try {
            Page<RoleDTO> page = roleService.queryRoleList(dto);
            return Result.with(page);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }


    @ApiOperation("查询全量角色信息列表")
    @PostMapping("/queryAllRoleList")
    @RequiresPermissions({"role:queryAll"})
    public Result<Map<String,Object>> queryAllRoleList(){
        try {
            List<RoleDTO> list = roleService.queryAllRoleList();

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",list);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }

    /**
     * 校验角色ID
     * @param roleId
     * @return
     */
    public void checkRoleId(Long roleId){
        if(roleId==null || roleId ==0){
            throw new BizException("MS_100004","角色ID");
        }
        //角色校验
        RoleDTO tmpRole = roleService.findSysRoleById(roleId);
        if(tmpRole == null){
            throw new BizException("MS_100002","角色ID");
        }
    }

    /**
     * 校验角色名称
     * @param roleName
     * @return
     */
    public void checkRoleName(String roleName){
        if(StringUtils.isEmpty(roleName)){
            throw new BizException("MS_100001","角色名称");
        }
    }

}
