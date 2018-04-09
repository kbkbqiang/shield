package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class UserLoginDTO implements Serializable{
    private static final long serialVersionUID = -6822855242912836248L;

    @NotBlank(message = "用户名不能为空")
    private String userName;    //用户名称
    @NotBlank(message = "密码不能为空")
    private String password;    //密码
    private String loginIp;     //登录IP
    //@NotNull(message = "是否有验证码不能为空")
    private Boolean haveSmsCode; //是否有验证码
    private String smsCode;     //短信验证码
}
