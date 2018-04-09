package com.niiwoo.shield.manage.web.vo.user.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysLoginLogExportReqVO {
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("分页的大小")
    private Integer pageSize = 20;

}
