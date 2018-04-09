package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum RelationTypeEnum {

    RELATIONTYPE_MSG((byte) 1, "后台通知"),
    OTHER((byte) 2, "其它");
    private final Byte code;
    private final String desc;

}
