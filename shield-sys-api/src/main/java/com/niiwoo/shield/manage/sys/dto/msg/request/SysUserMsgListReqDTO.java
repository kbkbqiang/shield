package com.niiwoo.shield.manage.sys.dto.msg.request;

import com.niiwoo.tripod.provider.dto.request.PageRequestDTO;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class SysUserMsgListReqDTO extends PageRequestDTO {
    private Long userId;
    private Byte readStatus;
}
