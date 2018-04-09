package com.niiwoo.shield.manage.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class UpdatePasswordVO implements Serializable{
    private static final long serialVersionUID = 2173341122708656334L;

    @NotNull(message = "用户ID不能为空")
    private Long userId;     //用户ID
    @NotBlank(message = "新密码不能为空")
    private String password;    //密码
    private String oldPassword;  //旧密码
}
