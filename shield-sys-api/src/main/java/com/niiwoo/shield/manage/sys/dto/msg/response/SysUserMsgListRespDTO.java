package com.niiwoo.shield.manage.sys.dto.msg.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class SysUserMsgListRespDTO implements Serializable {
    /**
     * 用户系统通知ID
     */
    private Long id;
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
     * 更新人
     */
    private String updatedName;

    /**
     * 读取状态
     */
    private Byte readStatus;

    /**
     * 是否有附件
     */
    private Boolean hasAttach;

}
