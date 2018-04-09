package com.niiwoo.shield.manage.web.vo.msg.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@ApiModel
public class SysUserMessageDetailRespVO extends SysUserMessageListRespVO{
    @ApiModelProperty(value = "通知内容")
    private String msgContent;

    @ApiModelProperty(value = "附件列表")
    private List<AttachmentRespVO> attachmentList;
}
