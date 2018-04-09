package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.sys.dto.AuthCodeDTO;
import com.niiwoo.shield.manage.sys.dto.SysLoginLogDTO;

/**
 * Created by dell on 2017/12/22.
 * Description：shield-parent
 */
public interface UserLoginDubboService {

    /**
     * 记录登录失败相关数据至用户表
     * @param userName
     */
    void saveFailLoginData(String userName);


    /**
     * 添加登录日志
     * @param sysLoginLogDTO
     */
    void insertSysLoginLog(SysLoginLogDTO sysLoginLogDTO);

    /**
     * 记录登录成功相关数据至用户表
     * @param userName
     * @param loginIp
     */
    void saveSuccessLoginData(String userName,String loginIp);


    /**
     * 神盾发送短信验证码
     * @param requestDto
     * @return
     */
    AuthCodeDTO mgrSendSmsCode(AuthCodeDTO requestDto);
}
