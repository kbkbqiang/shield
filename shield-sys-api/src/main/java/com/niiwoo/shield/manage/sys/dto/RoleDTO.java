package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class RoleDTO implements Serializable{
    private static final long serialVersionUID = -6856000841581337666L;

    private Long roleId;         //角色ID
    private String roleName;        //角色名称
    private String description;     //角色描述
    private String createdUser;     //创建人
    private Date createdTime;       //创建时间
    private String updatedUser;     //修改人
    private Date updatedTime;       //修改时间

}
