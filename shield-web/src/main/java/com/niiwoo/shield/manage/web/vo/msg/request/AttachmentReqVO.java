package com.niiwoo.shield.manage.web.vo.msg.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tongjianbo  2017/11/27
 * @description 附件
 */
@Getter
@Setter
@ApiModel(value = "附件")
public class AttachmentReqVO {
    @ApiModelProperty("附件名称")
    private  String attachmentName;
    @ApiModelProperty("附件路径")
    private  String attachmentPath;

}
