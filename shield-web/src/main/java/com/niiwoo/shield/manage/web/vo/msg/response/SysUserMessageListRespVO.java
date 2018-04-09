package com.niiwoo.shield.manage.web.vo.msg.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ApiModel
public class SysUserMessageListRespVO implements Serializable{
    @ApiModelProperty(value = "用户系统通知ID")
    private Long id;
	
    @ApiModelProperty(value = "通知ID")
	private Long msgId;
    
    @ApiModelProperty(value = "通知标题")
    private String msgTitle;
    
    @ApiModelProperty(value = "发送时间")
	private Date sendTime;
    
    @ApiModelProperty(value = "发送人")
	private String updatedName;

    @ApiModelProperty(value = "读取状态")
    private Byte readStatus;
    
    @ApiModelProperty(value = "是否有附件")
    private Boolean hasAttach;

}
