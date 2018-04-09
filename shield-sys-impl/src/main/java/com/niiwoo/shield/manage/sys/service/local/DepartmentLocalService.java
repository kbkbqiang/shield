package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.base.constants.ManageSysConstants;
import com.niiwoo.shield.manage.sys.dao.entity.SysDepartment;
import com.niiwoo.shield.manage.sys.dao.mapper.SysDepartmentMapperExt;
import com.niiwoo.shield.manage.sys.dto.DepartmentDTO;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@Slf4j
@Service
public class DepartmentLocalService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private SysDepartmentMapperExt sysDepartmentMapperExt;


    /**
     * 检验参数
     * @param departmentName
     * @param departmentCode
     * @return
     */
    public void checkDepartmentParam(String departmentName, String departmentCode){
        if(StringUtils.isEmpty(departmentName)){
            throw new BizException("MS_100001", "部门名称");
        }
        if(StringUtils.isEmpty(departmentCode)){
            throw new BizException("MS_100001", "部门编号");
        }
        //部门编码校验
        SysDepartment department = findSysDepartmentByCode(departmentCode);
        if(department != null){
            throw new BizException("MS_100003", "部门编码");
        }
    }


    /**
     * 根据部门编号查询部门
     * @param departmentCode
     * @return
     */
    public SysDepartment findSysDepartmentByCode(String departmentCode){
        return sysDepartmentMapperExt.findSysDepartmentByCode(departmentCode);
    }


    /**
     * 添加部门
     * @param dto
     */
    @Transactional
    public void addDepartment(DepartmentDTO dto) {
        //参数校验
        checkDepartmentParam(dto.getDepartmentName(),dto.getDepartmentCode());

        //数据组装
        Long parentId = dto.getParentId();
        if(parentId==null || parentId ==0){
            dto.setLevel(1);
            dto.setParentId(Long.valueOf(0));  //无上级部门
        }else{
            //上级部门ID校验
            SysDepartment parentDepartment = sysDepartmentMapperExt.selectByPrimaryKey(parentId);
            if(parentDepartment == null){
                throw new BizException("MS_100002", "上级部门");
            }else{
                if(parentDepartment.getLevel() > ManageSysConstants.MAX_DEPARTMENT_LEVEL - 1){
                    throw new BizException("MS_100014", ManageSysConstants.MAX_DEPARTMENT_LEVEL);
                }
            }
            dto.setLevel(parentDepartment.getLevel()+1);
        }

        Integer sort = dto.getSort();
        if(sort==null) dto.setSort(1);    //默认排序编号为1

        try {
            Long id = snowflakeIdWorker.nextId();
            dto.setDepartmentId(id);
            //创建新部门
            SysDepartment department = new SysDepartment();
            BeanUtils.copyProperties(dto, department);
            department.setCreatedUser(dto.getCreatedUser());
            department.setUpdatedUser(dto.getCreatedUser());
            Integer count = sysDepartmentMapperExt.insertSelective(department);
            if (count==0) {
                throw new BizException("MS_100005", "创建新部门");
            }
        }catch(Exception e){
            log.error("创建新部门操作失败{}",e.getMessage(), e);
            throw new BizException("MS_100005", "创建新部门");
        }
    }


    /**
     * 参数校验
     * @param departmentId
     * @return
     */
    public void checkDepartmentId(Long departmentId){
        if(departmentId==null || departmentId ==0){
            throw new BizException("MS_100004", "部门ID");
        }
        //部门ID校验
        SysDepartment tmpDepartment = sysDepartmentMapperExt.selectByPrimaryKey(departmentId);
        if(tmpDepartment == null){
            throw new BizException("MS_100002", "部门");
        }
    }


    /**
     * 删除部门
     * @param departmentId
     * @return
     */
    public void dropDepartment(Long departmentId){
        //参数校验
        checkDepartmentId(departmentId);

        try {
            List<Long> departmentIds = new ArrayList<Long>();
            departmentIds.add(departmentId);
            //查询子部门集合
            List<Long> childDeptIds = queryChildDepartmentIds(departmentId);
            if(childDeptIds!=null && !childDeptIds.isEmpty()) {
                departmentIds.addAll(childDeptIds);
            }
            Integer userCount = queryUserCountByDepartmentIds(departmentIds);
            if(userCount>0){
                throw new BizException("MS_100009", "当前部门或下级部门已关联用户");
            }

            //更新部门
            Integer count = deleteSysDepartmentByIds(departmentIds);
            if (count == 0) {
                throw new BizException("MS_100005", "更新部门");
            }
        }catch(Exception e){
            log.error("更新部门操作失败{}",e.getMessage(), e);
            throw new BizException("MS_100005", "更新部门");
        }
    }


    /**
     * 根据部门id批量删除
     * @param departmentIds
     * @return
     */
    @Transactional
    public Integer deleteSysDepartmentByIds(List<Long> departmentIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("departmentIds",departmentIds);
        return sysDepartmentMapperExt.deleteSysDepartmentByIds(params);
    }


    /**
     * 检查参数
     * @param departmentId
     * @param departmentName
     */
    public void checkDeptUpdateParam(Long departmentId, String departmentName){
        checkDepartmentId(departmentId);
        if(StringUtils.isEmpty(departmentName)){
            throw new BizException("MS_100001", "部门名称");
        }
    }

    /**
     * 更新部门信息
     * @param dto
     * @return
     */
    @Transactional
    public void updateDepartment(DepartmentDTO dto) {
        checkDeptUpdateParam(dto.getDepartmentId(),dto.getDepartmentName());

        //数据组装
        Long parentId = dto.getParentId();
        if(parentId==null || parentId ==0){
            dto.setLevel(1);
            dto.setParentId(Long.valueOf(0));  //无上级部门
        }else{
            //上级部门ID校验
            SysDepartment parentDepartment = sysDepartmentMapperExt.selectByPrimaryKey(parentId);
            if(parentDepartment == null){
                throw new BizException("MS_100002", "部门");
            }else{
                if(parentDepartment.getLevel() > ManageSysConstants.MAX_DEPARTMENT_LEVEL - 1){
                    throw new BizException("MS_100014", ManageSysConstants.MAX_DEPARTMENT_LEVEL);
                }
            }
            dto.setLevel(parentDepartment.getLevel()+1);
        }

        //默认排序编号为1
        if(dto.getSort()==null)  dto.setSort(1);

        try {
            //更新部门
            SysDepartment department = new SysDepartment();
            BeanUtils.copyProperties(dto, department);
            department.setUpdatedUser(dto.getUpdatedUser());

            Integer count = sysDepartmentMapperExt.updateByPrimaryKeySelective(department);
            if (count == 0) {
                throw new BizException("MS_100005", "更新部门");
            }
        }catch(Exception e){
            log.error("更新部门操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "更新部门");
        }
    }


    /**
     * 查询部门详细信息
     * @param departmentId
     * @return
     */
    public Map<String,Object> queryDepartmentDetail(Long departmentId){
        //参数校验
        if(departmentId==null || departmentId ==0){
            throw new BizException("MS_100004", "部门ID");
        }

        try {
            //查询部门详情
            SysDepartment department = sysDepartmentMapperExt.selectByPrimaryKey(departmentId);
            if(department == null){
                throw new BizException("MS_100002", "部门");
            }

            List<Long> departmentIds = new ArrayList<Long>();
            departmentIds.add(departmentId);
            //查询子部门集合
            List<Long> childDeptIds = queryChildDepartmentIds(departmentId);
            if(childDeptIds!=null && !childDeptIds.isEmpty()) {
                departmentIds.addAll(childDeptIds);
            }
            Integer userCount = queryUserCountByDepartmentIds(departmentIds);
            boolean deleteEnable = true;
            if(userCount>0){
                deleteEnable = false;
            }

            Map<String,Object> dataMap = new HashMap<String,Object>();
            DepartmentDTO departmentDTO = new DepartmentDTO();
            BeanUtils.copyProperties(department, departmentDTO);
            dataMap.put("department",departmentDTO);
            dataMap.put("deleteEnable",deleteEnable);
            return dataMap;

        }catch(Exception e){
            log.error("查询部门详细信息操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "查询部门详细信息");
        }
    }


    /**
     * 统计部门用户数
     * @param departmentIds
     * @return
     */
    public Integer queryUserCountByDepartmentIds(List<Long> departmentIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("departmentIds",departmentIds);
        return sysDepartmentMapperExt.queryUserCountByDepartmentIds(params);
    }


    /**
     * 查询上级部门下拉列表-树状结构
     * @return
     */
    public List<DepartmentDTO> queryParentDeptDownList(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("maxLevel", ManageSysConstants.MAX_DEPARTMENT_LEVEL);
        //查询上级部门下拉列表
        List<DepartmentDTO> deptList = queryDeptTreeList(params);
        return deptList;
    }


    /**
     * 根据部门ID查询下级部门ID集合
     * @param departmentId
     * @return
     */
    public List<Long> queryChildDepartmentIds(Long departmentId){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("departmentId", departmentId);
        List childIdList = getChildrenDeptIds(params);
        return childIdList;
    }


    /**
     * 查询树状部门列表
     * @return
     */
    public List<DepartmentDTO> queryDeptTreeList(Map<String,Object> params){
        //全量列表，顶级节点查询
        List<SysDepartment> deptList = sysDepartmentMapperExt.queryDeptTreeTopList(params);
        if(deptList==null || deptList.isEmpty()){
            return new ArrayList<DepartmentDTO>();
        }

        List<DepartmentDTO> resultList = new ArrayList<DepartmentDTO>();
        for(SysDepartment dept : deptList){
            DepartmentDTO departmentDTO = new DepartmentDTO();
            BeanUtils.copyProperties(dept, departmentDTO);

            params.put("departmentId",dept.getDepartmentId());
            //子节点查询
            List<DepartmentDTO> childrenDeptList = getChildrenDeptList(params);
            if(childrenDeptList!=null && childrenDeptList.size()>00)
                departmentDTO.setChildren(childrenDeptList);
            resultList.add(departmentDTO);
        }
        return resultList;
    }


    /**
     * 下级部门查询
     * @param params
     * @return
     */
    public List<DepartmentDTO> getChildrenDeptList(Map<String,Object> params){
        List<DepartmentDTO> resultList = new ArrayList<DepartmentDTO>();
        //子菜单列表，树结构
        List<SysDepartment> childrenDeptList = sysDepartmentMapperExt.queryDeptTreeChildrenList(params);
        if(childrenDeptList!=null && !childrenDeptList.isEmpty()){
            for(SysDepartment dept : childrenDeptList){
                DepartmentDTO deptDTO = new DepartmentDTO();
                BeanUtils.copyProperties(dept, deptDTO);
                //递归
                params.put("departmentId",dept.getDepartmentId());
                List<DepartmentDTO> childList = getChildrenDeptList(params);
                deptDTO.setChildren(childList);
                resultList.add(deptDTO);
            }
        }
        return resultList;
    }


    /**
     * 查询下级部门ID集合
     * @param params
     * @return
     */
    public List<Long> getChildrenDeptIds(Map<String,Object> params){
        List<Long> childIdList = new ArrayList<Long>();
        //子菜单列表，树结构
        List<SysDepartment> childrenDeptList = sysDepartmentMapperExt.queryDeptTreeChildrenList(params);
        if(childrenDeptList!=null && !childrenDeptList.isEmpty()){
            for(SysDepartment dept : childrenDeptList){
                childIdList.add(dept.getDepartmentId());
                //递归
                params.put("departmentId",dept.getDepartmentId());
                List<Long> childIds = getChildrenDeptIds(params);
                childIdList.addAll(childIds);
            }
        }
        return childIdList;
    }


    /**
     * 根据departmentId查询部门信息
     * @param departmentId
     * @return
     */
    public DepartmentDTO queryDepartmentById(Long departmentId) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        SysDepartment sysDepartment = sysDepartmentMapperExt.selectByPrimaryKey(departmentId);
        BeanUtils.copyProperties(sysDepartment, departmentDTO);
        return departmentDTO;
    }
}
