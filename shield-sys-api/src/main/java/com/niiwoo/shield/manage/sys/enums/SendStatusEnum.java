package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dell on 2017/9/23.
 */
@Getter
@AllArgsConstructor
public enum SendStatusEnum {

    NOT_SEND((byte) 0, "未发送"),
    SENDED((byte) 1, "已发送");

    private final Byte code;
    private final String desc;

}
