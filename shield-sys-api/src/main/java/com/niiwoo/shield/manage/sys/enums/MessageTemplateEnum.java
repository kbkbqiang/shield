package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MessageTemplateEnum {

    SD_MANAGE_LOGIN_VERIFICATION_CODE((byte) 1, "sd.verify.code", "神盾后台登录验证码");

    private Byte templateCode;
    private String templateKey;
    private String templateTitle;

}
