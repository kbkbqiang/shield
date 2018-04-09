package com.niiwoo.shield.manage.web.vo.msg.request;

import com.niiwoo.tripod.web.vo.PageRequestVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@ApiModel
public class SysUserMessageListQueryReqVO  extends PageRequestVO {

	@ApiModelProperty(value = "读取状态：1未读，2已读 null全部")
    private Byte readStatus;

}
