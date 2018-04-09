package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dell on 2017/12/11.
 * Description：civet-parent
 */
@Getter
@AllArgsConstructor
public enum SmsRecordTypeEnum {

    LOGIN_SMS_CODE((byte) 1, "登录短信验证码");

    private final Byte code;
    private final String desc;

}
