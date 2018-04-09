package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysDepartment;

import java.util.List;
import java.util.Map;

public interface SysDepartmentMapperExt extends SysDepartmentMapper{

    /**
     * 根据部门编号查询部门
     * @param departmentCode
     * @return
     */
    SysDepartment findSysDepartmentByCode(String departmentCode);


    /**
     * 根据部门id批量删除
     * @param params
     * @return
     */
    Integer deleteSysDepartmentByIds(Map<String, Object> params);


    /**
     * 统计部门用户数
     * @param params
     * @return
     */
    Integer queryUserCountByDepartmentIds(Map<String, Object> params);


    /**
     * 顶级节点查询
     * @param params
     * @return
     */
    List<SysDepartment> queryDeptTreeTopList(Map<String, Object> params);


    /**
     * 子菜单列表，树结构
     * @param params
     * @return
     */
    List<SysDepartment> queryDeptTreeChildrenList(Map<String, Object> params);
}


















