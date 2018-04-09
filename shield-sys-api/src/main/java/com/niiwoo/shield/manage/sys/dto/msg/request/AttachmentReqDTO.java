package com.niiwoo.shield.manage.sys.dto.msg.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AttachmentReqDTO implements Serializable{
    //附件名称
    private String attachmentName;
    //附件路径
    private String attachmentPath;
}
