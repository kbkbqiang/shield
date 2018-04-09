package com.niiwoo.shield.manage.sys.dto.msg.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AttachmentRespDTO implements Serializable{
    //附件ID
    private Long attachmentId;
    //附件名称
    private String attachmentName;
    //附件路径
    private String attachmentPath;
}
