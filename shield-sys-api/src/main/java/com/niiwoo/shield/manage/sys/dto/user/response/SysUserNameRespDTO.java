package com.niiwoo.shield.manage.sys.dto.user.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SysUserNameRespDTO implements Serializable{
    private Long userId;     //用户ID
    private String realName;    //真实姓名
}
