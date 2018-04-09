package com.niiwoo.shield.manage.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    NOT_NEED_SMS_CODE((byte) 0, "不需要验证码登录"),
    NEED_SMS_CODE((byte) 1, "需要验证码登录");

    private Byte value;
    private String desc;
}
