package com.niiwoo.shield.manage.sys.dto.msg.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class SysMsgListRespDTO implements Serializable {
    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private String updatedName;

    /**
     * 是否有附件
     */
    private Boolean hasAttach;

}
