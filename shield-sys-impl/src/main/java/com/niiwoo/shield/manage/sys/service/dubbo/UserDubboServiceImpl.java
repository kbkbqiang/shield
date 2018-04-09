package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysUser;
import com.niiwoo.shield.manage.sys.dto.QueryUserListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysUserNameReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysUserNameRespDTO;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.shield.manage.sys.service.local.UserLocalService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 用户管理模块
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Slf4j
@Service(version = "1.0.0")
public class UserDubboServiceImpl implements UserDubboService {

    @Autowired
    private UserLocalService userLocalService;

    /**
     * 添加员工
     * @param userDTO
     * @return
     */
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        return userLocalService.addUser(userDTO);
    }


    /**
     * 根据userName查询user
     * @param userName
     * @return
     */
    @Override
    public UserDTO queryUserByUserName(String userName) {
        UserDTO userDTO = null;
        SysUser sysUser = userLocalService.queryUserByUserName(userName);
        if (sysUser!=null){
            userDTO = new UserDTO();
            BeanUtils.copyProperties(sysUser, userDTO);
            userDTO.setImageFile(sysUser.getHeadImgUrl());
        }
        return userDTO;
    }


    /**
     * 根据userCode查询user
     * @param userCode
     * @return
     */
    @Override
    public UserDTO queryUserByUserCode(String userCode) {
        UserDTO userDTO = null;
        SysUser sysUser = userLocalService.queryUserByUserCode(userCode);
        if (sysUser!=null){
            userDTO = new UserDTO();
            BeanUtils.copyProperties(sysUser, userDTO);
        }
        return userDTO;
    }


    /**
     * 更新用户信息
     * @param userDTO
     */
    @Override
    public void updateUser(UserDTO userDTO) {
        userLocalService.updateUser(userDTO);
    }


    /**
     * 查询用户
     * @param userId
     * @return
     */
    @Override
    public UserDTO queryUserByUserId(Long userId) {
        return userLocalService.queryUserByUserId(userId);
    }


    /**
     * 批量查询用户
     * @param userIds
     * @return
     */
    @Override
    public List<UserDTO> queryUsersByuserIds(List<Long> userIds) {
        return (userIds==null || userIds.isEmpty()) ? new ArrayList<>() : userLocalService.queryUsersByuserIds(userIds);
    }


    /**
     * 修改用户密码
     * @param userDTO
     */
    @Override
    public void resetPassword(UserDTO userDTO) {
        userLocalService.resetPassword(userDTO);
    }


    /**
     * 用户解锁
     * @param userId
     * @param principal
     */
    @Override
    public void unlockUser(Long userId, String principal) {
        userLocalService.unlockUser(userId, principal);
    }


    /**
     * 删除用户
     * @param userIds
     * @param principal
     */
    @Override
    public void dropUser(Long[] userIds, String principal) {
        userLocalService.dropUser(userIds, principal);
    }


    /**
     * 分页查询部门信息
     * @param dto
     * @return
     */
    @Override
    public Page<UserDTO> queryUserList(QueryUserListDTO dto) {
        return userLocalService.queryUserList(dto);
    }


    /**
     * 查询用户的菜单列表,树状结构
     * @param currentUserId
     * @return
     */
    @Override
    public List<RoleMenuDataDTO> queryMenusByUserId(Long currentUserId) {
        return userLocalService.queryMenusByUserId(currentUserId);
    }


    /**
     * 查询用户按钮权限列表
     * @param currentUserId
     * @param menuId
     * @return
     */
    @Override
    public Set<String> getButtonsByUserId(Long currentUserId, Long menuId) {
        return userLocalService.getButtonsByUserId(currentUserId, menuId);
    }

    @Override
    public Set<String> getPermissionsByUserName(String userName) {
        return userLocalService.getPermissionsByUserName(userName);
    }

    @Override
    public Map<Integer, SysUserNameRespDTO> queryUserRealNameByUserIds(@NotEmpty List<SysUserNameReqDTO> userIdList) {
        Assert.notEmpty(userIdList, "userIdList不能为空");
        return userLocalService.queryUserRealNameByUserIds(userIdList);
    }
}



























