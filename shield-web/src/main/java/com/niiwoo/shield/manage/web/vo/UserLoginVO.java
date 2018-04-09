package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/12/9.
 * Description：civet-manage-parent
 */
@Setter
@Getter
public class UserLoginVO implements Serializable{
    private static final long serialVersionUID = -2319267929306719649L;

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("是否有验证码")
    @NotNull(message = "是否有验证码不能为空")
    private Boolean haveSmsCode;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("登录IP")
    private String loginIp;
}
