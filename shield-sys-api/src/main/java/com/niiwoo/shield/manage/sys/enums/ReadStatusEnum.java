package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dell on 2017/9/23.
 */
@Getter
@AllArgsConstructor
public enum ReadStatusEnum {

    NOT_READ((byte) 1, "未读"),
    READ((byte) 2, "已读");

    private final Byte code;
    private final String desc;
}
