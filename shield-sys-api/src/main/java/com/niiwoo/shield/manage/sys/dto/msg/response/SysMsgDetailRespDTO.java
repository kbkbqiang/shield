package com.niiwoo.shield.manage.sys.dto.msg.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SysMsgDetailRespDTO extends SysMsgListRespDTO{
    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 附件列表
     */
    private List<AttachmentRespDTO> attachmentRespDTOList;

    /**
     * 部门ID列表
     */
    private List<Long> departmentIdList;
}
