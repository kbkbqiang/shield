package com.niiwoo.shield.manage.sys.service.dubbo;


import com.alibaba.dubbo.config.annotation.Service;

import com.niiwoo.shield.manage.sys.dto.AuthCodeDTO;
import com.niiwoo.shield.manage.sys.dto.SysLoginLogDTO;

import com.niiwoo.shield.manage.sys.dto.SysParamDTO;
import com.niiwoo.shield.manage.sys.dto.SysSmsTemplateDTO;
import com.niiwoo.shield.manage.sys.enums.MessageTemplateEnum;
import com.niiwoo.shield.manage.sys.enums.SysParamKeyEnum;
import com.niiwoo.shield.manage.sys.service.UserLoginDubboService;
import com.niiwoo.shield.manage.sys.service.local.SysParamLocalService;
import com.niiwoo.shield.manage.sys.service.local.SysSmsTemplateLocalService;
import com.niiwoo.shield.manage.sys.service.local.UserLoginLocalService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dell on 2017/12/22.
 * Description：shield-parent
 */
@Slf4j
@Service(version = "1.0.0")
public class UserLoginDubboServiceImpl implements UserLoginDubboService {

    @Autowired
    private UserLoginLocalService userLoginLocalService;
    @Autowired
    private SysParamLocalService sysParamLocalService;
    @Autowired
    private SysSmsTemplateLocalService sysSmsTemplateLocalService;

    /**
     * 记录登录失败相关数据至用户表
     *
     * @param userName
     */
    @Override
    public void saveFailLoginData(String userName) {
        userLoginLocalService.saveFailLoginData(userName);
    }


    /**
     * 添加登录日志
     *
     * @param sysLoginLogDTO
     */
    @Override
    public void insertSysLoginLog(SysLoginLogDTO sysLoginLogDTO) {
        userLoginLocalService.insertSysLoginLog(sysLoginLogDTO);
    }


    /**
     * 记录登录成功相关数据至用户表
     *
     * @param userName
     * @param loginIp
     */
    @Override
    public void saveSuccessLoginData(String userName, String loginIp) {
        userLoginLocalService.saveSuccessLoginData(userName, loginIp);
    }


    /**
     * 神盾发送短信验证码
     *
     * @param authCodeDTO
     * @return
     */
    @Override
    public AuthCodeDTO mgrSendSmsCode(AuthCodeDTO authCodeDTO) {

        //获取短信模板内容
        String templateCode = MessageTemplateEnum.SD_MANAGE_LOGIN_VERIFICATION_CODE.getTemplateKey();
        SysSmsTemplateDTO sysSmsTemplateDTO = sysSmsTemplateLocalService.queryByTemplateCode(templateCode);
        if (sysSmsTemplateDTO == null) {
            return null;
        }
        //查询系统参数配置
        SysParamDTO sysParamDTO = sysParamLocalService.queryByParamKey(SysParamKeyEnum.SMS_CODE_COUNT.getKey());
        Integer limitCount = null;
        if (sysParamDTO == null) {
            limitCount = 0;
        } else {
            limitCount = StringUtils.isNotBlank(sysParamDTO.getParamValue()) ? Integer.valueOf(sysParamDTO.getParamValue()) : 0;
        }
        return userLoginLocalService.mgrSendSmsCode(authCodeDTO, limitCount, sysSmsTemplateDTO.getTemplateValue());
    }
}
