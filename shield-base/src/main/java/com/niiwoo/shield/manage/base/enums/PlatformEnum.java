package com.niiwoo.shield.manage.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台枚举
 * Created by dell on 2017/8/16.
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    PLATFORM_SD_WEB_APP(1L, Byte.valueOf("1"), "新app", 888888L),
    PLATFORM_SD_MANAGE(2L, Byte.valueOf("2"), "神盾后台", 888888L);

    private Long platformUserId;

    private Byte platformId;

    private String desc;

    private Long accountId;

}
