package com.niiwoo.shield.manage.sys.service.local;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.civet.manage.trade.dto.request.AuditWorkManagerSaveReqDTO;
import com.niiwoo.civet.manage.trade.dto.request.FollowWorkManagerReqDTO;
import com.niiwoo.civet.manage.trade.service.afterloan.FollowWorkManagerDubboService;
import com.niiwoo.civet.manage.trade.service.beforeloan.AuditWorkManagerDubboService;
import com.niiwoo.shield.manage.sys.config.WorkerInterfaceProperties;
import com.niiwoo.shield.manage.sys.dao.entity.SysUserRole;
import com.niiwoo.shield.manage.sys.dao.mapper.SysRoleMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserRoleMapperExt;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.enums.WorkTypeEnum;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户角色关联关系
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Slf4j
@Service
public class UserRoleLocalService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private WorkerInterfaceProperties workerInterfaceProperties;
    @Autowired
    private SysUserMapperExt sysUserMapperExt;
    @Autowired
    private SysRoleMapperExt sysRoleMapperExt;
    @Autowired
    private SysUserRoleMapperExt sysUserRoleMapperExt;
    @Reference(version = "1.0.0")
    private AuditWorkManagerDubboService auditWorkManagerDubboService;
    @Reference(version = "1.0.0")
    private FollowWorkManagerDubboService followWorkManagerDubboService;

    /**
     * 添加用户角色关联关系
     * @param userId
     * @param roleIds
     * @param principal
     * @param enableFlag
     */
    @Transactional
    public void addUserRoles(Long userId,Long[] roleIds,String principal,Boolean enableFlag){
        for(Long roleId : roleIds){
            addUserRole(userId,roleId,principal);
        }

        //检查审核员、跟踪员权限
        Set<Long> oldRoleIds = new HashSet<Long>();
        checkWorkTypeUser(userId,oldRoleIds, Arrays.asList(roleIds),false,true);
    }


    /**
     * 取消用户的所有角色权限
     * @param userIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    public void clearUserRoles(Long[] userIds, String principal, Boolean oldEnableFlag,Boolean newEnableFlag){
        for(Long userId : userIds){
            Long[] roleIds = new Long[0];
            updateUserRoles(userId, roleIds, principal, true, false);
        }
    }


    /**
     * 更新用户角色关联信息
     * @param userId
     * @param roleIds
     * @param principal
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    @Transactional
    public void updateUserRoles(Long userId, Long[] roleIds, String principal, Boolean oldEnableFlag,Boolean newEnableFlag){
        List<SysUserRole> userRoles = sysUserRoleMapperExt.getUserRolesByUserId(userId);
        Set<Long> oldRoleIds = new HashSet<Long>();
        for(SysUserRole userRole : userRoles){
            oldRoleIds.add(userRole.getRoleId());
        }

        //若旧角色列表不包含新的，则新增
        for(Long roleId : roleIds){
            if(!oldRoleIds.contains(roleId)) {
                addUserRole(userId, roleId, principal);
            }
        }

        //若新角色列表不包含旧的，则删除
        List<Long> newRoleIds = Arrays.asList(roleIds);
        for(Long roleId : oldRoleIds){
            if(!newRoleIds.contains(roleId)) {
                deleteUserRole(userId, roleId);
            }
        }

        //检查审核员、跟踪员权限
        checkWorkTypeUser(userId, oldRoleIds, newRoleIds, oldEnableFlag, newEnableFlag);
    }


    /**
     * 更新工作人员列表
     * @param workType
     * @param userId
     * @param operateFlag
     */
    public void updateWorkTaskUser(Integer workType,Long userId,Integer operateFlag){
        UserDTO userDTO = sysUserMapperExt.findSysUserById(userId);
        if(userDTO!=null) {
            if (operateFlag == 1) {
                //添加工作人员
                addWorkTaskUser(workType, userDTO);
            } else {
                //删除工作人员
                deleteWorkTaskUser(workType, userDTO);
            }
        }
    }

    /**
     * 添加工作人员
     * @param workType
     * @param userDTO
     */
    public void addWorkTaskUser(Integer workType, UserDTO userDTO){
        //通知业务端，添加极速借审核人员
        if (WorkTypeEnum.FL_AUDIT_TASK.getWorkType().equals(workType)) {
            // 添加极速借审核人员
            AuditWorkManagerSaveReqDTO workManager = new AuditWorkManagerSaveReqDTO();
            workManager.setManagerId(userDTO.getUserId());
            workManager.setManagerName(userDTO.getRealName());
            workManager.setManagerCode(userDTO.getUserCode());
            workManager.setMobileNo(userDTO.getMobile());
            auditWorkManagerDubboService.save(workManager);
        //通知业务端，添加极速借跟踪人员
        } else if (WorkTypeEnum.FL_FOLLOW_TASK.getWorkType().equals(workType)) {
            //添加极速借跟踪人员
            FollowWorkManagerReqDTO workManager = new FollowWorkManagerReqDTO();
            workManager.setManagerId(userDTO.getUserId());
            workManager.setManagerName(userDTO.getRealName());
            workManager.setManagerCode(userDTO.getUserCode());
            workManager.setMobileNo(userDTO.getMobile());
            followWorkManagerDubboService.save(workManager);
        }
    }


    /**
     * 删除工作人员
     * @param workType
     * @param userDTO
     */
    public void deleteWorkTaskUser(Integer workType, UserDTO userDTO){
        if(WorkTypeEnum.FL_AUDIT_TASK.getWorkType().equals(workType)){
            //删除极速借审核人员
            auditWorkManagerDubboService.deleteById(userDTO.getUserId());
        }else if(WorkTypeEnum.FL_FOLLOW_TASK.getWorkType().equals(workType)){
            //删除极速借跟踪人员
            followWorkManagerDubboService.deleteById(userDTO.getUserId());
        }
    }


    /**
     * 添加用户角色
     * @param userId
     * @param roleId
     * @param principal
     */
    @Transactional
    public void addUserRole(Long userId, Long roleId, String principal){
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        SysUserRole ur = sysUserRoleMapperExt.getUserRoleByParam(userRole);
        if(ur==null) {
            userRole.setId(snowflakeIdWorker.nextId());
            userRole.setCreatedUser(principal);
            userRole.setUpdatedUser(principal);
            sysUserRoleMapperExt.insertSelective(userRole);
        }
    }


    /**
     * 删除用户角色
     * @param userId
     * @param roleId
     */
    @Transactional
    public void deleteUserRole(Long userId, Long roleId){
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        sysUserRoleMapperExt.deleteSysUserRole(userRole);
    }


    /**
     * 根据角色名称查询角色ID
     * @param roleName
     * @return
     */
    public Long queryRoleIdByName(String roleName){
        Long roleId = null;
        RoleDTO role = sysRoleMapperExt.querySysRoleByName(roleName);
        if(role!=null){
            roleId = role.getRoleId();
        }
        return roleId;
    }


    /**
     * 检查审核员、跟踪员权限
     * @param userId
     * @param oldRoleIds
     * @param newRoleIds
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    public void checkWorkTypeUser(Long userId,Set<Long> oldRoleIds,List<Long> newRoleIds, Boolean oldEnableFlag,Boolean newEnableFlag){
        WorkTypeEnum[] workTypeEnums = WorkTypeEnum.values();
        for(WorkTypeEnum workTypeEnum : workTypeEnums) {
            checkWorkTaskUser(workTypeEnum,userId, oldRoleIds, newRoleIds, oldEnableFlag, newEnableFlag);
        }
    }


    /**
     * 检查
     * @param workTypeEnum
     * @param userId
     * @param oldRoleIds
     * @param newRoleIds
     * @param oldEnableFlag
     * @param newEnableFlag
     */
    public void checkWorkTaskUser(WorkTypeEnum workTypeEnum,Long userId,Set<Long> oldRoleIds,List<Long> newRoleIds, Boolean oldEnableFlag,Boolean newEnableFlag){
        String roleName = workTypeEnum.getRoleName();
        Integer operateFlag = 0;
        Long auditRoleId = queryRoleIdByName(roleName);
        if(auditRoleId!=null) {
            if (newRoleIds.contains(auditRoleId) && !oldRoleIds.contains(auditRoleId) && newEnableFlag) {
                operateFlag = 1;
            } else if (!newRoleIds.contains(auditRoleId) && oldRoleIds.contains(auditRoleId) && oldEnableFlag) {
                operateFlag = 2;
            } else if (newRoleIds.contains(auditRoleId) && oldRoleIds.contains(auditRoleId) && !newEnableFlag.equals(oldEnableFlag)) {
                if (newEnableFlag) {
                    operateFlag = 1;
                } else {
                    operateFlag = 2;
                }
            }

            if (operateFlag != 0) {
                //更新工作人员列表
                updateWorkTaskUser(workTypeEnum.getWorkType(),userId, operateFlag);
            }
        }
    }


    /**
     * 根据userId查询角色ids
     * @param userId
     * @return
     */
    public Set<Long> queryRoleIdsByUserId(Long userId){
        List<SysUserRole> userRoles = sysUserRoleMapperExt.getUserRolesByUserId(userId);
        if(userRoles==null || userRoles.isEmpty()){
            return new HashSet<Long>();
        }
        Set<Long> roleIds = new HashSet<Long>();
        for(SysUserRole userRole : userRoles) {
            Long roleId = userRole.getRoleId();
            roleIds.add(roleId);
        }
        return roleIds;
    }


    /**
     * 根据userName查询角色ids
     * @param userName
     * @return
     */
    public Set<Long> queryRoleIdsByUserName(String userName){
        List<SysUserRole> userRoles = sysUserRoleMapperExt.getUserRolesByUserName(userName);
        if(userRoles==null || userRoles.isEmpty()){
            return new HashSet<Long>();
        }
        Set<Long> roleIds = new HashSet<Long>();
        for(SysUserRole userRole : userRoles) {
            Long roleId = userRole.getRoleId();
            roleIds.add(roleId);
        }
        return roleIds;
    }

    public RoleDTO getUserRoleByName(Long userId, String roleName) {
        return sysUserRoleMapperExt.getUserRoleByRoleName(userId, roleName);
    }
}
