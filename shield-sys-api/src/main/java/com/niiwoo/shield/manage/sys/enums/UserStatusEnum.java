package com.niiwoo.shield.manage.sys.enums;

import lombok.Getter;

/**
 * Created by dell on 2017/8/10.
 */
@Getter
public enum UserStatusEnum {

    USED((byte) 0,"已启用"),
    STOPED((byte)1,"已停用"),
    LOCKED((byte)2,"已锁定"),
    DELETED((byte)3,"已删除");

    private final Byte statusCode;
    private final String statusDesc;

    UserStatusEnum(Byte statusCode, String statusDesc){
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }
}
