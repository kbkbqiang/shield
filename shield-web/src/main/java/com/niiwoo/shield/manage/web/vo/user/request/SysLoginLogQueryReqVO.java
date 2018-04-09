package com.niiwoo.shield.manage.web.vo.user.request;


import com.niiwoo.tripod.web.vo.PageRequestVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysLoginLogQueryReqVO extends PageRequestVO {
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户ID")
    private Long userId;
}
