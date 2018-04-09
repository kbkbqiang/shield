package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysPermission;
import com.niiwoo.shield.manage.sys.dao.entity.SysRole;
import com.niiwoo.shield.manage.sys.dao.entity.SysRolePermission;
import com.niiwoo.shield.manage.sys.dao.entity.SysUserRole;
import com.niiwoo.shield.manage.sys.dao.mapper.*;
import com.niiwoo.shield.manage.sys.dto.PermissionDTO;
import com.niiwoo.shield.manage.sys.dto.QueryRoleListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 角色管理
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@Slf4j
@Service
public class RoleLocalService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private SysUserRoleMapperExt sysUserRoleMapperExt;

    @Autowired
    private SysRoleMapperExt sysRoleMapperExt;

    @Autowired
    private SysRolePermissionMapperExt sysRolePermissionMapperExt;
    @Autowired
    private SysPermissionMapperExt permissionMapperExt;

    @Autowired
    private SysRoleMenuMapperExt sysRoleMenuMapperExt;


    /**
     * 添加角色
     * @param dto
     */
    @Transactional
    public void addRole(RoleDTO dto){
        //校验角色名称是否已存在
        RoleDTO tmpRole = sysRoleMapperExt.querySysRoleByName(dto.getRoleName());

        if(tmpRole!=null){
            throw new BizException("MS_100003", "角色名称");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        try {
            Long id = snowflakeIdWorker.nextId();
            role.setRoleId(id);
            Integer count = sysRoleMapperExt.insertSelective(role);
            if (count==0) {
                throw new BizException("MS_100005");
            }

            //为角色授予默认权限
            grantDefaultPermission(role.getRoleId(), role.getCreatedUser());
        }catch(Exception e){
            log.error("添加角色操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "添加角色");
        }
    }


    /**
     * 为角色授予默认权限
     * @param roleId
     * @param principal
     */
    @Transactional
    public void grantDefaultPermission(Long roleId, String principal){
        Date now = new Date();
        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setCreatedUser(principal);
        rolePermission.setUpdatedUser(principal);
        rolePermission.setCreatedTime(now);
        rolePermission.setUpdatedTime(now);

        //角色默认权限: 查询当前登录用户有权限的菜单，查询当前登录用户某菜单下的按钮权限
//        String[] defaultPermissions = {"user:queryMenus","user:queryButtons"};
//
//        for(String defaultPermission : defaultPermissions){
//            List<PermissionDTO> sysPermissions = permissionMapperExt.findSysPermissionByValue(defaultPermission);
//            if(sysPermissions!=null && !sysPermissions.isEmpty()) {
//                PermissionDTO sysPermission = sysPermissions.get(0);
//                rolePermission.setId(snowflakeIdWorker.nextId());
//                rolePermission.setPermissionId(sysPermission.getPermissionId());
//                sysRolePermissionMapperExt.insertSelective(rolePermission);
//            }
//        }
    }


    /**
     * 修改角色
     * @param dto
     */
    @Transactional
    public void updateRole(RoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        //更新角色
        try {
            Integer count = sysRoleMapperExt.updateByPrimaryKeySelective(role);
            if (count == 0) {
                throw new BizException("MS_100005", "更新角色");
            }
        }catch(Exception e){
            log.error("更新角色操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "更新角色");
        }
    }


    /**
     * 删除角色
     * @param roleId
     */
    @Transactional
    public Set<Long> dropRole(Long roleId){
        Set<Long> userIds = getUserIdsByRoleId(roleId);
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(roleId);
        for(Long userId : userIds){
            //删除用户与角色的关联关系
            userRole.setUserId(userId);
            sysUserRoleMapperExt.deleteSysUserRole(userRole);
        }
        //删除角色的菜单权限
        sysRoleMenuMapperExt.deleteRoleMenusByRoleId(roleId);
        //删除角色的资源权限
        sysRolePermissionMapperExt.deleteRolePermissionsByRoleId(roleId);
        //删除角色
        sysRoleMapperExt.deleteByPrimaryKey(roleId);

        return userIds;
    }


    public RoleDTO findSysRoleById(Long roleId){
        RoleDTO roleDTO = null;
        RoleDTO sysRole = sysRoleMapperExt.findSysRoleById(roleId);
        if (sysRole != null){
            roleDTO = new RoleDTO();
            BeanUtils.copyProperties(sysRole, roleDTO);
        }
        return roleDTO;
    }


    public List<RoleDTO> queryAllRoleList(){
        return sysRoleMapperExt.queryAllRoleList();
    }


    /**
     * 根据角色id查询用户id列表
     * @param roleId
     * @return
     */
    public Set<Long> getUserIdsByRoleId(Long roleId){
        return sysRoleMapperExt.getUserIdsByRoleId(roleId);
    }


    /**
     * 分页查询角色总记录数
     * @param page
     * @return
     */
    public Integer queryRoleListCount(Page page){
        return sysRoleMapperExt.queryRoleListCount(page);
    }


    /**
     * 分页查询角色列表
     * @param dto
     * @return
     */
    public Page<RoleDTO> queryRoleList(QueryRoleListDTO dto){
        Page<RoleDTO> page = dto.getPage();
        Integer totalRecord = queryRoleListCount(page);
        page.setTotalRecord(totalRecord);
        List<RoleDTO> resultList = new ArrayList<RoleDTO>();
        if(totalRecord > 0){
            List<RoleDTO> roleList = sysRoleMapperExt.queryRoleList(page);
            for(RoleDTO role : roleList){
                RoleDTO roleDTO = new RoleDTO();
                BeanUtils.copyProperties(role, roleDTO);
                resultList.add(roleDTO);
            }
        }
        page.setResult(resultList);
        return page;
    }


    /**
     * 通过userId查询角色列表
     * @param userId
     * @return
     */
    public List<RoleDTO> queryRolesByUserId(Long userId){
        List<RoleDTO> roleDTOS = new ArrayList<>();
        List<SysRole> sysRoles = sysRoleMapperExt.queryRolesByUserId(userId);
        for (SysRole sysRole : sysRoles) {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(sysRole, roleDTO);
            roleDTOS.add(roleDTO);
        }
        return roleDTOS;
    }


}
