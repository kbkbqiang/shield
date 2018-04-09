package com.niiwoo.shield.manage.sys.dto.user.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SysUserNameReqDTO implements Serializable{
    private Integer index;
    private Long userId;     //用户ID
}
