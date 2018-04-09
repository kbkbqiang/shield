package com.niiwoo.shield.manage.web.vo.msg.request;

import com.niiwoo.tripod.web.vo.PageRequestVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@ApiModel
public class SysMessageListQueryReqVO extends PageRequestVO {

    @ApiModelProperty(value = "通知标题")
    private String msgTitle;

    @ApiModelProperty(value = "发送时间min")
    private Date sendStartTime;

    @ApiModelProperty(value = "发送时间max")
    private Date sendEndTime;

}
