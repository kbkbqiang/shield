package com.niiwoo.shield.manage.sys.dto;

import com.niiwoo.shield.manage.base.entity.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 后台管理系统角色列表查询
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class QueryRoleListDTO implements Serializable{
    private static final long serialVersionUID = -8488601100051461084L;

    private String roleName;    //角色名称
    private Integer pageSize;    //每页记录条数
    private Integer pageNumber;  //页码

    private Page<RoleDTO> page = new Page<RoleDTO>();

    private Page<RoleDTO> initialPage(){
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.addParam("roleName",roleName);
        return page;
    }

    public Page<RoleDTO> getPage(){
        if(Objects.nonNull(page.getParams()) && !page.getParams().isEmpty()){
            return page;
        }
        return initialPage();
    }
}
