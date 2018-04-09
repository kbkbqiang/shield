package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuhecheng
 * 部门信息实体类
 */
@Setter
@Getter
public class DepartmentDTO implements Serializable{
    private static final long serialVersionUID = 9012425723523713629L;

    private Long departmentId;  //部门ID
    private String departmentName; //部门名称
    private Long parentId;       //上级部门ID
    private String departmentCode;  //部门编号
    private Integer level;          //部门层级
    private Integer sort;          //排序编号
    private String createdUser;      //创建人
    private Date createdTime;        //创建时间
    private String updatedUser;      //修改人
    private Date updatedTime;        //修改时间

    //下级部门列表，树状结构
    private List<DepartmentDTO> children = new ArrayList<>();
}
