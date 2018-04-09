package com.niiwoo.shield.manage.web.vo;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by dell on 2017/12/21.
 * Description：shield-parent
 */
@Setter
@Getter
@ApiModel
public class QueryUsersRequestVO implements Serializable{
    private static final long serialVersionUID = 6160980048311336589L;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("部门ID")
    private Long departmentId;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("状态 0 已启用，1 已停用，2 已锁定")
    private Integer status;

    @ApiModelProperty("每页记录条数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNumber;
}
