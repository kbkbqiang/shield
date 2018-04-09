package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dell on 2017/9/23.
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum {

    SYSTEM_MSG((byte) 1, "系统通知");

    private final Byte msgTypeCode;
    private final String msgTypeDesc;

}
