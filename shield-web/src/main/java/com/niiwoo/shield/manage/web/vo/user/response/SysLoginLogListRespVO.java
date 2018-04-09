package com.niiwoo.shield.manage.web.vo.user.response;


import com.niiwoo.tripod.web.vo.PageRequestVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SysLoginLogListRespVO {
    @ApiModelProperty("系统登录日志ID")
    private Long id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("登录时间")
    private Date loginTime;
    @ApiModelProperty("登录IP")
    private String loginIp;
    @ApiModelProperty("登录状态 1 成功，2 失败")
    private Byte status;
    @ApiModelProperty("状态描述")
    private String msg;
}
