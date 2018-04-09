package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.DepartmentDTO;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
public interface DepartmentDubboService {

    /**
     * 添加部门
     * @param departmentDTO
     */
    void addDepartment(DepartmentDTO departmentDTO);


    /**
     * 修改部门信息
     * @param departmentDTO
     */
    void updateDepartment(DepartmentDTO departmentDTO);


    /**
     * 删除部门信息
     * @param departmentId
     */
    void dropDepartment(Long departmentId);


    /**
     * 查询部门详细信息
     * @param departmentId
     * @return
     */
    Map<String,Object> queryDepartmentDetail(Long departmentId);


    /**
     * 查询树状部门列表
     * @param params
     * @return
     */
    List<DepartmentDTO> queryDeptTreeList(Map<String, Object> params);


    /**
     * 查询上级部门下拉列表-树状结构
     * @return
     */
    List<DepartmentDTO> queryParentDeptDownList();


    /**
     * 根据departmentId查询部门信息
     * @param departmentId
     * @return
     */
    DepartmentDTO findSysDepartmentById(Long departmentId);
}
