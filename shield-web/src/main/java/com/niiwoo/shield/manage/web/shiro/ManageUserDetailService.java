package com.niiwoo.shield.manage.web.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.niiwoo.shield.manage.sys.dto.RoleDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.enums.UserStatusEnum;
import com.niiwoo.shield.manage.sys.service.PermissionDubboService;
import com.niiwoo.shield.manage.sys.service.RoleDubboService;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.tripod.web.shiro.CustomByteSource;
import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import com.niiwoo.tripod.web.shiro.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangwanli on 2017/10/12.
 */
@Slf4j
@Component
public class ManageUserDetailService implements UserDetailService {

    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;
    @Reference(version = "1.0.0")
    private RoleDubboService roleDubboService;
    @Reference(version = "1.0.0")
    private PermissionDubboService permissionDubboService;

    /**
     * 认证时调用，查询用户信息
     * @param userName
     * @return
     */
    @Override
    public UserAuthPrincipal loadPrincipalByUserName(String userName) {
        UserAuthPrincipal userAuthPrincipal = new UserAuthPrincipal();
        UserDTO userDTO = userDubboService.queryUserByUserName(userName);
        if (userDTO != null) {
            Byte status = userDTO.getStatus();
            //帐号已停用
            if(UserStatusEnum.STOPED.getStatusCode().equals(status)) {
                throw new DisabledAccountException();

            //帐号已删除
            } else if(UserStatusEnum.DELETED.getStatusCode().equals(status)) {
                throw new UnknownAccountException();

            //帐号被锁定
            } else if(UserStatusEnum.LOCKED.getStatusCode().equals(status)) {
                throw new LockedAccountException();

            //状态正常
            } else {
                userAuthPrincipal.setUserId(String.valueOf(userDTO.getUserId()));
                userAuthPrincipal.setPrincipal(userDTO.getUserName());
                userAuthPrincipal.setCredentials(userDTO.getPassword());
                userAuthPrincipal.setSalt(userDTO.getSalt());
                userAuthPrincipal.setReserve(userDTO.getMobile());          //业务保留域，传递手机号
            }
        }else{
            //帐号不存在
            throw new UnknownAccountException();
        }
        log.info("登录成功===》" + JSON.toJSONString(userAuthPrincipal));
        return userAuthPrincipal;
    }

    @Override
    public UserAuthPrincipal loadPrincipalByOpenId(String openId, Byte platformId) {
        return null;
    }


    /**
     * 授权时调用，查询用户角色列表
     * @param userId
     * @return
     */
    @Override
    public List<String> queryRoles(String userId) {
        log.info("queryRoles（userId）=====>" + userId);
        List<RoleDTO> roleDTOS = roleDubboService.queryRolesByUserId(Long.valueOf(userId));
        List<String> roleNames = new ArrayList<>();
        for (RoleDTO roleDTO : roleDTOS) {
            roleNames.add(roleDTO.getRoleName());
        }
        log.info("queryRoles（roleNames）=====>" + roleNames);
        return roleNames;
    }


    /**
     * 授权时调用，查询用户资源列表
     * @param userId
     * @return
     */
    @Override
    public List<String> queryPermissions(String userId) {
        log.info("queryPermissions（userId）=====>" + userId);
        List<String> strings = permissionDubboService.queryPermissions(Long.valueOf(userId));
        log.info("queryPermissions（权限列表）=====>" + strings);
        return strings;
    }

}
