package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.sys.dto.MenuDTO;
import com.niiwoo.shield.manage.sys.dto.PermissionDTO;
import com.niiwoo.shield.manage.sys.enums.PermissionStatusEnum;
import com.niiwoo.shield.manage.sys.service.MenuManageDubboService;
import com.niiwoo.shield.manage.sys.service.PermissionDubboService;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/22.
 */
@Slf4j
@RestController
@RequestMapping("/permissions")
@Api(tags = "资源管理", description = "/permissions", position = 7)
public class PermissionManageController extends BaseController {

    @Reference(version = "1.0.0")
    private PermissionDubboService permissionService;

    @Reference(version = "1.0.0")
    private MenuManageDubboService menuService;

    /**
     * 添加新的资源
     * @param dto
     * @return
     */
    @ApiOperation("添加新的资源")
    @PostMapping("/addPermission")
    @RequiresPermissions({"permission:add"})
    public Empty addPermission(@RequestBody PermissionDTO dto){
        //参数校验
        checkPermissionParam(dto);
        dto.setCreatedUser(getCurrentUser().getPrincipal());
        dto.setUpdatedUser(getCurrentUser().getPrincipal());
        permissionService.addPermission(dto);
        return Empty.getInstance();
    }


    /**
     * 编辑资源信息
     * @param dto
     * @return
     */
    @ApiOperation("编辑资源信息")
    @PostMapping("/editPermission")
    @RequiresPermissions({"permission:edit"})
    public Empty editPermission(@RequestBody PermissionDTO dto){
        //校验更新参数
        checkPermissionUpdateParam(dto);
        dto.setUpdatedUser(getCurrentUser().getPrincipal());
        permissionService.updatePermission(dto);
        return Empty.getInstance();
    }

    /**
     * 删除资源
     * @param dto
     * @return
     */
    @ApiOperation("编辑资源信息")
    @PostMapping("/dropPermission")
    @RequiresPermissions({"permission:drop"})
    public Empty dropPermission(@RequestBody PermissionDTO dto){
        Long permissionId = dto.getPermissionId();
        if(permissionId==null || permissionId ==0){
            throw new BizException("MS_100004","资源ID");
        }

        try {
            /*//校验该资源是否已授权
            Integer count = permissionService.queryRolePermissionCountByPermId(permissionId);
            if(count>0){
                return newResultByEnum(CANNOT_DROP,"当前资源已授权");
            }*/
            //删除资源信息
            permissionService.deletePermission(permissionId);
            return Empty.getInstance();
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100004","资源ID");
        }
    }

    /**
     * 根据菜单ID查询资源列表
     * @param dto
     * @return
     */
    @ApiOperation("根据菜单ID查询资源列表")
    @PostMapping("/queryPermissionsByMenuId")
    @RequiresPermissions({"permission:queryByMenuId"})
    public Result<Map<String,Object>> queryPermissionsByMenuId(@RequestBody PermissionDTO dto){
        try {
            List<PermissionDTO> list = permissionService.queryPermissionsByMenuId(dto.getMenuId());

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",list);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }

    /**
     *  校验新资源参数
     * @param dto
     */
    public Empty checkPermissionParam(PermissionDTO dto){
        String permissionName = dto.getPermissionName();
        String permissionValue = dto.getPermissionValue();
        Long menuId = dto.getMenuId();
        if(StringUtils.isEmpty(permissionName)){
            throw new BizException("MS_100001","资源名称");
        }
        if(StringUtils.isEmpty(permissionValue)){
            throw new BizException("MS_100004","资源内容");
        }
        checkMenuId(dto.getMenuId());
        //校验权限是否已存在
        PermissionDTO perm = new PermissionDTO();
        perm.setPermissionValue(dto.getPermissionValue());
        perm.setMenuId(menuId);
        Integer permCount = permissionService.findSysPermCountByCondition(perm);
        if(permCount > 0){
            throw new BizException("MS_100003","资源");
        }
        return Empty.getInstance();
    }

    /**
     * 检验资源更新参数
     * @param dto
     * @return
     */
    public void checkPermissionUpdateParam(PermissionDTO dto){
        String permissionName = dto.getPermissionName();
        String permissionValue = dto.getPermissionValue();
        if(StringUtils.isEmpty(permissionName)){
            throw new BizException("MS_100001","资源名称");
        }
        if(StringUtils.isEmpty(permissionValue)){
            throw new BizException("MS_100004","资源内容");
        }

        Byte status = dto.getStatus();
        if(!status.equals(PermissionStatusEnum.USED.getStatusCode())
                && !status.equals(PermissionStatusEnum.STOPED.getStatusCode())){
            throw new BizException("MS_100004","资源状态");
        }
    }

    /**
     * 校验菜单ID
     * @param menuId
     * @return
     */
    public void checkMenuId(Long menuId){
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004","菜单ID");
        }

        //角色ID校验
        MenuDTO tmpMenu = menuService.findSysMenuById(menuId);
        if(tmpMenu == null){
            throw new BizException("MS_100002","菜单");
        }
    }

}
