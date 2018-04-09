package com.niiwoo.shield.manage.sys.dto.msg.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Setter
@Getter
public class SysUserMsgDetailReqDTO implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "后台通知ID不能为空")
    private Long msgId;
}
