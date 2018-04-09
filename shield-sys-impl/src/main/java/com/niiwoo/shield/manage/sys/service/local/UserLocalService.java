package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.base.constants.RedisConstants;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysRole;
import com.niiwoo.shield.manage.sys.dao.entity.SysUser;
import com.niiwoo.shield.manage.sys.dao.entity.SysUserRole;
import com.niiwoo.shield.manage.sys.dao.mapper.SysRolePermissionMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserRoleMapperExt;
import com.niiwoo.shield.manage.sys.dto.QueryUserListDTO;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysUserNameReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysUserNameRespDTO;
import com.niiwoo.shield.manage.sys.enums.UserStatusEnum;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 员工管理模块
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Slf4j
@Service
public class UserLocalService {


    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysUserMapperExt sysUserMapperExt;
    @Autowired
    private RoleLocalService roleLocalService;
    @Autowired
    private UserRoleLocalService userRoleLocalService;
    @Autowired
    private SysRolePermissionMapperExt sysRolePermissionMapperExt;
    @Autowired
    private SysUserRoleMapperExt sysUserRoleMapperExt;

    @Autowired
    private RolePermissionLocalService rolePermissionLocalService;
    @Autowired
    private RoleMenuLocalService roleMenuLocalService;


    /**
     * 创建新员工
     * @param dto
     * @return
     */
    @Transactional
    public UserDTO addUser(UserDTO dto) {
        try {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dto, user);
            user.setUserId(snowflakeIdWorker.nextId());
            dto.setUserId(user.getUserId());
            Date now = new Date();
            user.setCreatedTime(now);
            user.setUpdatedTime(now);

            Integer count = sysUserMapperExt.insertSelective(user);
            if (count==0) {
                throw new BizException("MS_100005", "创建新员工");
            }
        }catch(Exception e){
            log.error("创建新员工操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "创建新员工");
        }

        //清空密码再返回
        dto.setPassword("");
        return dto;
    }


    /**
     * 更新员工信息
     * @param dto
     * @return
     */
    @Transactional
    public void updateUser(UserDTO dto) {
        try {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dto, user);
            user.setHeadImgUrl(dto.getImageFile());
            user.setUpdatedTime(new Date());
            //更新用户
            Integer count = sysUserMapperExt.updateByPrimaryKeySelective(user);
            if (count==0) {
                throw new BizException("MS_100005", "更新员工信息");
            }
        }catch(Exception e){
            log.error("更新员工信息操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "更新员工信息");
        }
    }


    /**
     * 删除用户
     * @param userIds
     * @param principal
     */
    @Transactional
    public void dropUser(Long[] userIds, String principal) {
        Integer count = 0;
        for (Long userId : userIds) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setStatus(UserStatusEnum.DELETED.getStatusCode());
            user.setUpdatedUser(principal);
            Date now = new Date();
            user.setUpdatedTime(now);
            Integer result = sysUserMapperExt.updateByPrimaryKeySelective(user);
            count += result;
        }

        if(count==0){
            throw new BizException("MS_100005", "删除用户");
        }
    }


    /**
     * 修改用户密码
     * @param userDTO
     * @return
     */
    public void resetPassword(UserDTO userDTO){
        try {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(userDTO, user);
            Date now = new Date();
            user.setUpdatedTime(now);

            Integer count = sysUserMapperExt.resetPassword(user);
            if (count == 0) {
                throw new BizException("MS_100005", "修改用户密码");
            }
        }catch(Exception e){
            log.error("修改用户密码操作失败{}", e.getMessage(), e);
            throw new BizException("MS_100005", "修改用户密码");
        }
    }



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


    /**
     * 根据角色集合查询【资源权限】列表
     * @param roleIds
     * @return
     */
    public Set<String> getPermissionsByRoles(Set<Long> roleIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleIds",roleIds);
        //查询角色的资源权限数据
        Set<String> permissions = sysRolePermissionMapperExt.getPermissionsByUserRoles(params);
        if(permissions==null){
            return new HashSet<String>();
        }

        return permissions;
    }

    /**
     * 根据用户名称的【资源权限】数据
     * @param userName
     * @return
     */
    public Set<String> getPermissionsByUserName(String userName){
        Set<Long> roleIds = queryRoleIdsByUserName(userName);
        if(roleIds==null || roleIds.isEmpty()){
            return new HashSet<String>();
        }
        //查询角色的资源权限数据
        Set<String> permissions = getPermissionsByRoles(roleIds);
        return permissions;
    }
    /**
     * 通过userName查询
     * @param userName
     * @return
     */
    public SysUser queryUserByUserName(String userName) {
        return sysUserMapperExt.findSysUserByName(userName);
    }


    /**
     * 通过userCode查询
     * @param userCode
     * @return
     */
    public SysUser queryUserByUserCode(String userCode) {
        return sysUserMapperExt.findSysUserByCode(userCode);
    }


    /**
     * 通过userId查询
     * @param userId
     * @return
     */
    public UserDTO queryUserByUserId(Long userId) {
        return sysUserMapperExt.findSysUserById(userId);
    }


    /**
     * 查询总记录数
     * @param page
     * @return
     */
    public Integer queryUserListCount(Page page){
        return sysUserMapperExt.queryUserListCount(page);
    }


    /**
     * 分页查询用户列表
     * @param dto
     * @return
     */
    public Page<UserDTO> queryUserList(QueryUserListDTO dto){
        Page<UserDTO> page = dto.getPage();
        Integer totalRecord = queryUserListCount(page);
        page.setTotalRecord(totalRecord);
        List<UserDTO> resultList = new ArrayList<UserDTO>();
        if(totalRecord>0){
            List<UserDTO> userList = sysUserMapperExt.queryUserList(page);
            if(userList!=null && !userList.isEmpty()) {
                for(UserDTO userDTO : userList) {
                    //查询角色集合
                    List<RoleDTO> roles = roleLocalService.queryRolesByUserId(userDTO.getUserId());
                    StringBuffer roleNames = new StringBuffer("");
                    int count = 0;
                    for (RoleDTO role : roles) {
                        if (count != 0) {
                            roleNames.append(",");
                        }
                        roleNames.append(role.getRoleName());
                        count++;
                    }
                    userDTO.setRoleNames(roleNames.toString());
                    //清空密码再返回
                    userDTO.setPassword("");
                    resultList.add(userDTO);
                }
            }
        }
        page.setResult(resultList);
        return page;
    }


    /**
     * 查询树状【菜单权限】数据
     * @param userId
     * @return
     */
    public List<RoleMenuDataDTO> queryMenusByUserId(Long userId){
        Set<Long> roleIds = userRoleLocalService.queryRoleIdsByUserId(userId);
        if(roleIds==null || roleIds.isEmpty()){
            return new ArrayList<RoleMenuDataDTO>();
        }

        //全量菜单列表，顶级节点查询
        List<RoleMenuDataDTO> menuList = roleMenuLocalService.queryMenuListByRoles(roleIds);
        if(menuList==null || menuList.isEmpty()){
            return new ArrayList<RoleMenuDataDTO>();
        }
        return menuList;
    }

    /**
     * 根据角色集合和菜单ID查询【按钮资源权限】列表
     * @param roleIds
     * @return
     */
    public Set<String> getButtonsByRoles(Set<Long> roleIds, Long menuId){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleIds",roleIds);
        params.put("menuId",menuId);
        //查询角色的资源权限数据
        Set<String> permissions = sysRolePermissionMapperExt.getPermissionsByUserRoles(params);
        if(permissions==null){
            return new HashSet<String>();
        }
        return permissions;
    }

    /**
     * 根据用户ID和菜单ID查询【按钮资源权限】列表
     * @param userId
     * @return
     */
    public Set<String> getButtonsByUserId(Long userId ,Long menuId){
        Set<Long> roleIds = userRoleLocalService.queryRoleIdsByUserId(userId);
        if(roleIds==null || roleIds.isEmpty()){
            return new HashSet<String>();
        }

        //查询角色的资源权限数据
        Set<String> permissions = getButtonsByRoles(roleIds,menuId);
        return permissions;
    }


    /**
     * 更新用户最后登录IP和时间
     * @param user
     */
    @Transactional
    public void updateLoginData(SysUser user){
        sysUserMapperExt.updateUserLoginData(user);
    }

    /**
     * 用户解锁
     * @param userId
     * @param principal
     */
    @Transactional
    public void unlockUser(Long userId, String principal){
        UserDTO userDTO = sysUserMapperExt.findSysUserById(userId);
        if(userDTO==null){
            throw new BizException("MS_100002", "用户");
        }

        String redisKey = RedisConstants.SD_MANAGER_LOGIN_RETRY_TIMES_PREFIX + userDTO.getUserName();
        if(redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
        //记录登录日志
        userDTO.setErrorTimes(0);
        userDTO.setStatus(UserStatusEnum.USED.getStatusCode());
        userDTO.setUpdatedUser(principal);
        Date currentTime = new Date();
        userDTO.setUpdatedTime(currentTime);

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDTO, sysUser);
        sysUserMapperExt.updateByPrimaryKeySelective(sysUser);
    }


    /**
     * 查询黑灰名单添加人列表
     */
    public List<SysUser> getDarkgreyAdderlist() {
        List<SysUser> userList = sysUserMapperExt.queryDarkgreyAdderlist();
        return userList;
    }

    /**
     * 根据用户id查询用户的真实姓名
     *
     * @param userIdList
     * @return
     */
    public Map<Integer,SysUserNameRespDTO> queryUserRealNameByUserIds(List<SysUserNameReqDTO> userIdList) {
        Map<Integer,SysUserNameRespDTO> sysUserNameDTOMap = new HashMap<>();
        for (SysUserNameReqDTO sysUserNameReqDTO : userIdList) {
            SysUserNameRespDTO sysUserNameRespDTO = sysUserMapperExt.selectUserRealNameByUserId(sysUserNameReqDTO.getUserId());
            if(sysUserNameRespDTO == null){
                sysUserNameRespDTO = new SysUserNameRespDTO();
                sysUserNameRespDTO.setUserId(sysUserNameReqDTO.getUserId());
            }
            sysUserNameDTOMap.put(sysUserNameReqDTO.getIndex(),sysUserNameRespDTO);
        }
        return sysUserNameDTOMap;
    }


    /**
     * 批量查询用户
     * @param userIds
     * @return
     */
    public List<UserDTO> queryUsersByuserIds(List<Long> userIds) {
        return sysUserMapperExt.queryUsersByuserIds(userIds);
    }
}
