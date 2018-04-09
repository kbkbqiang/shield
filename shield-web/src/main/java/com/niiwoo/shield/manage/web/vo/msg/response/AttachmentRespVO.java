package com.niiwoo.shield.manage.web.vo.msg.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel(value = "附件")
public class AttachmentRespVO {
    @ApiModelProperty("附件ID")
    private Long attachmentId;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件路径")
    private String attachmentPath;

}
