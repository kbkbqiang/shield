package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class MenuDTO implements Serializable{
    private static final long serialVersionUID = -1844610951832435217L;

    private Long menuId;  //菜单ID
    private String menuName; //菜单名称
    private String url;       //菜单URL
    private Long parentId; //上级菜单ID
    private Byte level;     //菜单层级
    private Integer sort;      //菜单排序编号
    private Byte isLeaf;    //是否叶子菜单
    private String createdUser;   //创建人
    private Date createdTime;     //创建时间
    private String updatedUser;   //修改人
    private Date updatedTime;     //修改时间

    //下级菜单列表，树状结构
    private List<MenuDTO> children;
}
