package com.niiwoo.shield.manage.web.enums;


/**
 * Created by dell on 2017/6/12.
 */
public enum RespMsgEnum {

    NO_SUPPORT_EMPTY("601", "{0}不能为空"),
    NO_EXISTS("602", "该{0}不存在"),
    ALREADY_EXISTS("603", "该{0}已存在"),
    INVALID_PARAM("604", "该{0}无效"),
    OPERATE_FAILED("605", "操作失败{0}"),
    IS_WRONG("606", "{0}错误"),
    ACCOUNT_STOP_USED("607", "{0}已停用"),
    ACCOUNT_IS_LOCKED("608", "请联系管理员解锁账号{0}"),
    CANNOT_DROP("609", "{0},不能删除"),
    NO_SUPPORT_SAME("610", "{0}不能相同"),
    ROLE_GRANT_FAIL("611", "用户角色授权失败{0}"),
    DROP_ROLE_GRANT_FAIL("612", "删除用户角色权限失败{0}"),
    SEND_SMS_CODE_FAIL("613", "发送验证码失败");

    private final String code;

    private final String msg;

    private RespMsgEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
