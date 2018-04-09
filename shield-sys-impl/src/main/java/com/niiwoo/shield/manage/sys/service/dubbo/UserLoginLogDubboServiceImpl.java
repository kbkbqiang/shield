package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogAddReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogQueryReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysLoginLogListRespDTO;
import com.niiwoo.shield.manage.sys.service.UserLoginDubboService;
import com.niiwoo.shield.manage.sys.service.UserLoginLogDubboService;
import com.niiwoo.shield.manage.sys.service.local.UserLoginLogLocalService;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service(version = "1.0.0")
public class UserLoginLogDubboServiceImpl implements UserLoginLogDubboService{
    @Autowired
    private UserLoginLogLocalService userLoginLogLocalService;

    @Override
    public Integer insertSysLoginLog(SysLoginLogAddReqDTO sysLoginLogAddReqDTO) {
        return userLoginLogLocalService.insertSysLoginLog(sysLoginLogAddReqDTO);
    }

    @Override
    public Page<SysLoginLogListRespDTO> queryLoginLogPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        return userLoginLogLocalService.queryLoginLogPageByCondition(sysLoginLogQueryReqDTO);
    }

    @Override
    public List<SysLoginLogListRespDTO> queryLoginLogListByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        return userLoginLogLocalService.queryLoginLogListByCondition(sysLoginLogQueryReqDTO);
    }

    @Override
    public Integer queryLoginLogTotalPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        return userLoginLogLocalService.queryLoginLogTotalPageByCondition(sysLoginLogQueryReqDTO);
    }
}
