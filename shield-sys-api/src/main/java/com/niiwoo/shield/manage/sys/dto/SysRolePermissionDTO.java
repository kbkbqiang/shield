package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Getter
@Setter
public class SysRolePermissionDTO implements Serializable{
    private static final long serialVersionUID = 8685867454601007211L;

    private Long id;             //主键ID
    private Long roleId;         //角色ID
    private Long permissionId;   //资源权限ID
    private String createdUser;     //创建人
    private Date createdTime;       //创建时间
    private String updatedUser;     //修改人
    private Date updatedTime;       //修改时间
}
