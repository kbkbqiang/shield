package com.niiwoo.shield.manage.sys.dto.user.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SysLoginLogAddReqDTO {
    private String userName;

    private Date loginTime;

    private String loginIp;

    private Byte status;

    private String msg;
}
