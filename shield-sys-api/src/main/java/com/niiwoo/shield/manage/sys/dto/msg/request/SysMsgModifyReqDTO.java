package com.niiwoo.shield.manage.sys.dto.msg.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SysMsgModifyReqDTO extends SysMsgAddReqDTO {
    @NotNull(message = "后台通知ID不能为空")
    private Long msgId;
}
