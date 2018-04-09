package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class AuthManageDTO implements Serializable{
    private static final long serialVersionUID = 5030442325076711953L;

    private Long roleId;         //角色ID
    private Long menuId;        //菜单ID
    private Long[] menuIds;     //菜单集合
    private Long[] permissionIds; //资源集合

}
