package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuhecheng
 * 角色菜单权限数据包装类，树状结构
 */
@Setter
@Getter
public class RoleMenuDataDTO implements Serializable{
    private static final long serialVersionUID = -6590286048786413871L;

    private Long menuId;  //菜单ID
    private String menuName; //菜单名称
    private String url;       //菜单URL
    private Long parentId; //上级菜单ID
    private Integer isLeaf;    //是否叶子菜单
    private Integer sort;     //菜单排序编号
    private Integer grantFlag; //授权标识 1 已授权，0 未授权

    private Long roleId;     //角色ID
    //子菜单
    private List<RoleMenuDataDTO> children;  //子菜单列表，树状结构

}
