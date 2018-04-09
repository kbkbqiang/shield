package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Desc: 删除标记，0:未删除，1：已删除
 * Create by ligang on 2017/11/16
 */
@Getter
@AllArgsConstructor
public enum DeleteFlagEnum {

    NOT_DELETE((byte) 0, "未删除"),
    DELETED((byte) 1, "已删除");

    private Byte value;
    private String desc;
}
