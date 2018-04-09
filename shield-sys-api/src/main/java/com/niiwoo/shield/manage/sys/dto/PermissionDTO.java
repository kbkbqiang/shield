package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuhecheng
 * 资源信息
 */
@Setter
@Getter
public class PermissionDTO implements Serializable{
    private static final long serialVersionUID = -8043246455249234879L;

    private Long permissionId;    //资源权限ID
    private String permissionName;   //资源权限名称
    private String permissionValue;   //权限内容
    private Long menuId;            //菜单ID
    private String description;        //权限描述
    private Byte status;             //状态 0 已启用，1 已停用
    private String createdUser;        //创建人
    private Date createdTime;           //创建时间
    private String updatedUser;         //修改人
    private Date updatedTime;           //修改时间


}
