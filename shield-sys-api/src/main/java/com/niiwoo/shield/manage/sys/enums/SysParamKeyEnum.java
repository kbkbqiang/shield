package com.niiwoo.shield.manage.sys.enums;

import com.niiwoo.tripod.provider.exception.NoSuchEnumValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: wuke
 * @Description:
 * @Date: Created in 16:35 2017/11/30
 */
@Getter
@AllArgsConstructor
public enum SysParamKeyEnum {

    NOTICE_DETAIL_PAGE_URL("niiwoo.notice.detail.page.url", "首页公告列表noticeURL"),
    SMS_CODE_COUNT("sms.code.count", "每日发送短信验证码限制次数"),
    HTML5_APP_URL_LOAN_MYLOANDETAIL("html5_app_url_loan_myloandetail", "我的借款-借款详情-html5页面地址");


    private final String key;
    private final String value;

    public static SysParamKeyEnum enumOf(Byte value) {
        for (SysParamKeyEnum sysParamKeyEnum : values()) {
            if (sysParamKeyEnum.value.equals(value)) {
                return sysParamKeyEnum;
            }
        }
        throw new NoSuchEnumValueException("SysParamKeyEnum:" + value);
    }
}
