package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysUser;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysUserNameRespDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapperExt extends SysUserMapper{

    /**
     * 查询用户
     * @param userId
     * @return
     */
    UserDTO findSysUserById(Long userId);


    /**
     * 修改用户密码
     * @param user
     * @return
     */
    Integer resetPassword(SysUser user);


    /**
     * 通过userName查询
     * @param userName
     * @return
     */
    SysUser findSysUserByName(String userName);


    /**
     * 通过userCode查询
     * @param userCode
     * @return
     */
    SysUser findSysUserByCode(String userCode);


    /**
     * 查询总记录数
     * @param page
     * @return
     */
    Integer queryUserListCount(Page page);


    /**
     * 分页查询用户列表
     * @param page
     * @return
     */
    List<UserDTO> queryUserList(Page<UserDTO> page);


    /**
     * 更新用户最后登录IP和时间
     * @param user
     */
    void updateUserLoginData(SysUser user);


    /**
     * 查询黑灰名单添加人列表
     * @return
     */
    List<SysUser> queryDarkgreyAdderlist();

    /**
     * 查询所有用户的userId
     * @return
     */
    List<Long> selectAllUserId();

    /**
     * 根据用户Id查询用户真实姓名
     * @param userId
     * @return
     */
    SysUserNameRespDTO selectUserRealNameByUserId(Long userId);

    /**
     * 批量查询用户
     * @param userIds
     * @return
     */
    List<UserDTO> queryUsersByuserIds(@Param("userIds") List<Long> userIds);
}