package com.niiwoo.shield.manage.base.constants;

/**
 * @Description: redis常量设置
 * @Author:LiJian
 * @Date:2017/4/27 20:14.
 */
public class RedisConstants {

    public static final String VERIFY_CODE_PREFIX = "verifyCode:"; // 验证码前缀

    public static final String SMS_CODE_COUNT_PREFIX = "smsCode_count:"; // 短信验证码计数前缀

    public static final int CODE_EXPIRE_TIME = 5 * 60; // 验证码过期时间（秒）

    public static final int COUNT_EXPIRE_TIME = 24 * 60 * 60; // 计数器过期时间（秒）

    public static final String SYS_PARAMS_PREFIX = "sys_params:";  // 系统参数前缀

    public static final String PROJECT_CONFIG_PREFIX = "project_config:"; // 项目配置前缀

    public static final String PORTRAIT_AUTH_COUNT_PREFIX = "portraitAuth_count:";     //肖像认证计数前缀

    public static final String BIND_BANK_CARD = "bindBankCard:";  //绑定银行卡前缀

    public static final String SD_MANAGER_LOGIN_RETRY_TIMES_PREFIX ="sdManagerLoginRetryTimes:"; //神盾登录重试计数前缀

    public static final String REGISTER_SUCCESS_COUNT = "register_success_count";//当日注册成功累计量，前缀

    public static final String SHOW_MYSELF_INFO_PREFIX = "showMyselfInfo:";    //app“我的”模块接口信息key前缀

    public static final String SCAN_AUTH_STATUS_PREFIX = "scanAuthStatus:";    //扫描更新用户认证状态key前缀

    public static final String ACTIVITY_FIGHT_LUCK_RED_ENVELOPS_PREFIX ="activityFightLuckRedEnvelops:"; //拼手气红包，前缀

    public static final String ACTIVITY_RECEIVE_REPEAT_RED_ENVELOPS_PREFIX ="activityReceiveRepeatRedEnvelops:"; //防重复领取红包，前缀
}
