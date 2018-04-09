package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@ApiModel
@Setter
@Getter
public class DepartmentVo implements Serializable{
    private static final long serialVersionUID = -7342435232742029083L;

    @ApiModelProperty("部门ID")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("上级部门ID")
    private Long parentId;

    @ApiModelProperty("部门编号")
    private String departmentCode;

    @ApiModelProperty("部门层级")
    private Integer level;

    @ApiModelProperty("排序编号")
    private Integer sort;

    @ApiModelProperty("创建人")
    private String createdUser;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("修改时间")
    private Date updatedTime;
}
