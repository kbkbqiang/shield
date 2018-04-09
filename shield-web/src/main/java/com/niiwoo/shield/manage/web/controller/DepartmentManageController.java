package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.sys.dto.DepartmentDTO;
import com.niiwoo.shield.manage.sys.service.DepartmentDubboService;
import com.niiwoo.shield.manage.web.vo.DepartmentVo;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */

@Slf4j
@RestController
@RequestMapping("/departments")
@Api(tags = "部门管理", description = "/departments", position = 2)
public class DepartmentManageController extends BaseController{

    @Reference(version = "1.0.0")
    private DepartmentDubboService departmentDubboService;


    @ApiOperation(value = "创建新部门")
    @PostMapping("/addDepartment")
    @RequiresPermissions({"department:add"})
    public Empty addDepartment(@RequestBody DepartmentVo request){
        log.info("创建新部门");
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(request, departmentDTO);
        departmentDTO.setCreatedUser(getCurrentUser().getPrincipal());
        departmentDTO.setUpdatedUser(getCurrentUser().getPrincipal());
        departmentDubboService.addDepartment(departmentDTO);
        return Empty.getInstance();
    }


    @ApiOperation(value = "修改部门信息")
    @PostMapping("/editDepartment")
    @RequiresPermissions({"department:edit"})
    public Empty editDepartment(@RequestBody DepartmentVo request){
        log.info("修改部门信息");
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(request, departmentDTO);
        departmentDTO.setUpdatedUser(getCurrentUser().getPrincipal());
        departmentDubboService.updateDepartment(departmentDTO);
        return Empty.getInstance();
    }


    @ApiOperation(value = "删除部门信息")
    @PostMapping("/dropDepartment")
    @RequiresPermissions({"department:drop"})
    public Empty dropDepartment(@RequestBody DepartmentVo request){
        log.info("删除部门信息");
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(request, departmentDTO);
        departmentDTO.setUpdatedUser(getCurrentUser().getPrincipal());
        departmentDubboService.dropDepartment(request.getDepartmentId());
        return Empty.getInstance();
    }


    @ApiOperation(value = "查询部门详情")
    @PostMapping("/queryDepartmentDetail")
    @RequiresPermissions({"department:queryDetail"})
    public Result queryDepartmentDetail(@RequestBody DepartmentVo request){
        log.info("查询部门详情");
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(request, departmentDTO);
        Map<String,Object> departmentDetail = departmentDubboService.queryDepartmentDetail(request.getDepartmentId());
        return Result.with(departmentDetail);
    }


    @ApiOperation(value = "查询部门列表(树状结构)")
    @PostMapping("/queryDeptTreeList")
    @RequiresPermissions({"department:queryTree"})
    public Result queryDeptTreeList(){
        //查询部门详情
        Map<String,Object> params = new HashMap<String,Object>();
        List<DepartmentDTO> list = departmentDubboService.queryDeptTreeList(params);

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("list",list);
        return Result.with(dataMap);
    }


    @ApiOperation(value = "查询上级部门下拉列表(树状结构)")
    @PostMapping("/queryParentDeptDownList")
    @RequiresPermissions({"department:queryParentDownlist"})
    public Result queryParentDeptDownList(){
        //查询上级部门下拉列表-树状结构
        List<DepartmentDTO> list = departmentDubboService.queryParentDeptDownList();

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("list",list);
        return Result.with(dataMap);
    }


}
