package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.sys.dto.DepartmentDTO;
import com.niiwoo.shield.manage.sys.service.DepartmentDubboService;
import com.niiwoo.shield.manage.sys.service.local.DepartmentLocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@Slf4j
@Service(version = "1.0.0")
public class DepartmentDubboServiceImpl implements DepartmentDubboService {

    @Autowired
    private DepartmentLocalService departmentLocalService;

    /**
     * 添加部门
     * @param departmentDTO
     */
    @Override
    public void addDepartment(DepartmentDTO departmentDTO) {
        departmentLocalService.addDepartment(departmentDTO);
    }


    /**
     * 修改部门信息
     * @param departmentDTO
     */
    @Override
    public void updateDepartment(DepartmentDTO departmentDTO) {
        departmentLocalService.updateDepartment(departmentDTO);
    }


    /**
     * 删除部门信息
     * @param departmentId
     */
    @Override
    public void dropDepartment(Long departmentId) {
        departmentLocalService.dropDepartment(departmentId);
    }


    /**
     * 查询部门详细信息
     * @param departmentId
     * @return
     */
    @Override
    public Map<String, Object> queryDepartmentDetail(Long departmentId) {
        return departmentLocalService.queryDepartmentDetail(departmentId);
    }


    /**
     * 查询树状部门列表
     * @param params
     * @return
     */
    @Override
    public List<DepartmentDTO> queryDeptTreeList(Map<String, Object> params) {
        return departmentLocalService.queryDeptTreeList(params);
    }


    /**
     * 查询上级部门下拉列表-树状结构
     * @return
     */
    @Override
    public List<DepartmentDTO> queryParentDeptDownList() {
        return departmentLocalService.queryParentDeptDownList();
    }


    /**
     * 根据departmentId查询部门信息
     * @param departmentId
     * @return
     */
    @Override
    public DepartmentDTO findSysDepartmentById(Long departmentId) {
        return departmentLocalService.queryDepartmentById(departmentId);
    }


}
