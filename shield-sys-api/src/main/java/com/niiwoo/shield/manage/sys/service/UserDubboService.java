package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.QueryUserListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysUserNameReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysUserNameRespDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
public interface UserDubboService {

    /**
     * 添加员工
     * @param userDTO
     * @return
     */
    UserDTO addUser(UserDTO userDTO);


    /**
     * 根据userName查询user
     * @param userName
     * @return
     */
    UserDTO queryUserByUserName(String userName);


    /**
     * 根据userCode查询user
     * @param userCode
     * @return
     */
    UserDTO queryUserByUserCode(String userCode);


    /**
     * 更新用户信息
     * @param userDTO
     */
    void updateUser(UserDTO userDTO);


    /**
     * 查询用户
     * @param userId
     * @return
     */
    UserDTO queryUserByUserId(Long userId);

    /**
     * 批量查询用户
     * @param userId
     * @return
     */
    List<UserDTO> queryUsersByuserIds(List<Long> userId);


    /**
     * 修改用户密码
     * @param userDTO
     */
    void resetPassword(UserDTO userDTO);


    /**
     * 用户解锁
     * @param userId
     * @param principal
     */
    void unlockUser(Long userId, String principal);


    /**
     * 删除用户
     * @param userIds
     * @param principal
     */
    void dropUser(Long[] userIds, String principal);


    /**
     * 分页查询部门信息
     * @param dto
     * @return
     */
    Page<UserDTO> queryUserList(QueryUserListDTO dto);


    /**
     * 查询用户的菜单列表,树状结构
     * @param currentUserId
     * @return
     */
    List<RoleMenuDataDTO> queryMenusByUserId(Long currentUserId);


    /**
     * 查询用户按钮权限列表
     * @param currentUserId
     * @param menuId
     * @return
     */
    Set<String> getButtonsByUserId(Long currentUserId, Long menuId);
    /**
     * 根据用户名称的【资源权限】数据
     * @param userName
     * @return
     */
    Set<String> getPermissionsByUserName(String userName);

    /**
     * 通过userIdList查询用户名
     * @param userIdList
     * @return
     */
    Map<Integer,SysUserNameRespDTO> queryUserRealNameByUserIds(List<SysUserNameReqDTO> userIdList);
}
