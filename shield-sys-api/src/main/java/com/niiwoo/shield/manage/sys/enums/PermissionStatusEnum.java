package com.niiwoo.shield.manage.sys.enums;

import lombok.Getter;

/**
 * Created by luosiwen on 2017/12/22.
 */
@Getter
public enum PermissionStatusEnum {

    USED(new Byte("0"),"已启用"),
    STOPED(new Byte("1"),"已停用");

    private final Byte statusCode;
    private final String statusDesc;

    PermissionStatusEnum(Byte statusCode,String statusDesc){
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }
}
