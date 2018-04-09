package com.niiwoo.shield.manage.web.vo.msg.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@ApiModel
public class SysMessageDetailQueryReqVO {
    @ApiModelProperty(value = "后台通知ID")
    @NotNull(message = "后台通知ID不能为空")
    private Long msgId;
}
