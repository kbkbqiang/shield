package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类型
 * Created by dell on 2017/8/16.
 */
@Getter
@AllArgsConstructor
public enum ValidateCodeEnum {

    SMSCODE(1, "短信验证码"),
    IMAGECODE(2, "图片验证码");

    private Integer type;
    private String desc;
}
