package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Setter
@Getter
public class RolePermissionDataDTO implements Serializable{
    private static final long serialVersionUID = 221258199114257458L;

    private Long permissionId;   //资源权限ID
    private String permissionName;  //资源权限名称
    private String permissionValue;  //资源权限内容
    private String description;   //资源说明
    private Integer grantFlag; //授权标识 1 已授权，0 未授权
    private Date createdTime;

}
