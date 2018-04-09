package com.niiwoo.shield.manage.web.vo.msg.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@ApiModel
public class SysMessageDelReqVO {

    @ApiModelProperty(value = "后台通知ID列表")
    @NotNull(message = "后台通知ID列表不能为空")
    @Size(min = 1, message = "后台通知ID列表不能为空")
    private List<Long> msgIdList;

}
