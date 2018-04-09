package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@ApiModel
@Setter
@Getter
public class UserVO implements Serializable {

    private static final long serialVersionUID = 3030531626346075837L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty("用户名称")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotEmpty(message = "真实姓名不能为空")
    @ApiModelProperty("真实姓名")
    private String realName;

    @NotEmpty(message = "用户编号不能为空")
    @ApiModelProperty("用户编号")
    private String userCode;

    @NotEmpty(message = "手机号码不能为空")
    @ApiModelProperty("手机号码")
    private String mobile;

    @NotEmpty(message = "邮箱地址不能为空")
    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("用户头像")
    private String imageFile;

    @NotEmpty(message = "部门ID不能为空")
    @ApiModelProperty("部门ID")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private  String departmentName;

    @ApiModelProperty("状态 0 已启用，1 已停用，2 已锁定")
    private Byte status;

    @ApiModelProperty("最后登录时间")
    private Date loginTime;

    @ApiModelProperty("最后登录IP")
    private String loginIp;

    @ApiModelProperty("密码错误次数")
    private Integer errorTimes;

    @ApiModelProperty("用户名锁定截止时间")
    private Date lockTime;

    @ApiModelProperty("创建人")
    private String createdUser;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("修改时间")
    private Date updatedTime;

    @ApiModelProperty("用户ids")
    private Long[] userIds;

    @ApiModelProperty("角色ids")
    private Long[] roleIds;

    @ApiModelProperty("角色名称，多个以英文逗号隔开")
    private String roleNames;
}
