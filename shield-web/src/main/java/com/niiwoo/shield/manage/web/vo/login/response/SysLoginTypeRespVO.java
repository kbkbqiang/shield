package com.niiwoo.shield.manage.web.vo.login.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "系统登录类型")
public class SysLoginTypeRespVO {
    @ApiModelProperty("系统登录类型：0：无需验证码登录 1：需要验证码登录")
    private Byte loginType;
}
