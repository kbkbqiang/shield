package com.niiwoo.shield.manage.sys.dto.user.request;

import com.niiwoo.tripod.provider.dto.request.PageRequestDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysLoginLogQueryReqDTO extends PageRequestDTO {
    private String userName;    //用户名称
    private Long userId;        //用户ID
}
