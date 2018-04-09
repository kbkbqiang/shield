package com.niiwoo.shield.manage.sys.dto.user.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class SysLoginLogListRespDTO implements Serializable{

    private Long id;

    private String userName;

    private Date loginTime;

    private String loginIp;

    private Byte status;

    private String msg;
}
