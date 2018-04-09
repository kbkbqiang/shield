package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.sys.dto.MenuDTO;
import com.niiwoo.shield.manage.sys.service.MenuManageDubboService;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Slf4j
@RestController
@RequestMapping("/menus")
@Api(tags = "菜单管理", description = "/menus", position = 5)
public class MenuManageController extends BaseController{

    @Reference(version = "1.0.0")
    private MenuManageDubboService menuManageDubboService;


    @ApiOperation("添加菜单")
    @PostMapping("/addMenu")
    @RequiresPermissions({"menu:add"})
    public Empty addMenu(@RequestBody MenuDTO dto){
        UserAuthPrincipal userAuthPrincipal = getCurrentUser();
        dto.setCreatedUser(userAuthPrincipal.getPrincipal());
        dto.setUpdatedUser(userAuthPrincipal.getPrincipal());
        menuManageDubboService.addMenu(dto);
        return Empty.getInstance();
    }


    @ApiOperation("编辑菜单信息")
    @PostMapping("/editMenu")
    @RequiresPermissions({"menu:edit"})
    public Empty editMenu(@RequestBody MenuDTO dto){
        UserAuthPrincipal userAuthPrincipal = getCurrentUser();
        dto.setUpdatedUser(userAuthPrincipal.getPrincipal());
        menuManageDubboService.updateMenu(dto);
        return Empty.getInstance();
    }


    @ApiOperation("查询菜单详情")
    @PostMapping("/queryMenuDetail")
    @RequiresPermissions({"menu:queryDetail"})
    public Result<Map<String,Object>> queryMenuDetail(@RequestBody MenuDTO dto){
        Long menuId = dto.getMenuId();
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004","菜单ID");
        }
        try {
            //查询菜单信息
            MenuDTO menu = menuManageDubboService.findSysMenuById(menuId);
            if(menu == null){
                throw new BizException("MS_100002","菜单");
            }
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("menu",menu);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }

    }


    @ApiOperation("删除菜单")
    @PostMapping("/dropMenu")
    @RequiresPermissions({"menu:drop"})
    public Empty dropMenu(@RequestBody MenuDTO dto){
        UserAuthPrincipal userAuthPrincipal = getCurrentUser();
        dto.setUpdatedUser(userAuthPrincipal.getPrincipal());
        menuManageDubboService.dropMenu(dto.getMenuId());
        return Empty.getInstance();
    }


    @ApiOperation("查询菜单列表(树状结构)")
    @PostMapping("/queryMenuTreeList")
    @RequiresPermissions({"menu:queryTree"})
    public Result<Map<String,Object>> queryMenuTreeList(){
        try {
            //查询菜单列表(树状结构)
            Map<String,Object> params = new HashMap<String,Object>();
            List<MenuDTO> list = menuManageDubboService.queryMenuTreeList(params);

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",list);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100015");
        }
    }


    @ApiOperation("查询上级菜单下拉列表(树状结构)")
    @PostMapping("/queryParentMenuDownList")
    @RequiresPermissions({"menu:queryParentDownlist"})
    public Result queryParentMenuDownList(){
        try {
            //查询菜单列表(树状结构)
            List<MenuDTO> list = menuManageDubboService.queryParentMenuDownList();

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",list);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100015");
        }
    }

}
