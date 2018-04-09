package com.niiwoo.shield.manage.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelStatusEnum {
    /**
     * 正常
     */
    NORMAL("N"),
    /**
     * 删除
     */
    DEL("D");

    private String value;

}
