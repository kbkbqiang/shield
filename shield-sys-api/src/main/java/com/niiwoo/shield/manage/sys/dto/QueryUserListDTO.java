package com.niiwoo.shield.manage.sys.dto;

import com.niiwoo.shield.manage.base.entity.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 后台管理系统用户列表查询
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class QueryUserListDTO implements Serializable{
    private static final long serialVersionUID = 2150110360489453794L;

    private String realName;    //真实姓名
    private String mobile;      //手机号码
    private Long departmentId;   //部门ID
    private Long roleId;        //角色ID
    private Integer status;     //状态 0 已启用，1 已停用，2 已锁定

    private Integer pageSize;    //每页记录条数
    private Integer pageNumber;  //页码

    private Page<UserDTO> page = new Page<UserDTO>();

    private Page<UserDTO> initialPage(){
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.addParam("realName",realName);
        page.addParam("mobile",mobile);
        page.addParam("departmentId",departmentId);
        page.addParam("roleId",roleId);
        page.addParam("status",status);
        return page;
    }

    public Page<UserDTO> getPage(){
        if(Objects.nonNull(page.getParams()) && !page.getParams().isEmpty()){
            return page;
        }
        return initialPage();
    }
}
