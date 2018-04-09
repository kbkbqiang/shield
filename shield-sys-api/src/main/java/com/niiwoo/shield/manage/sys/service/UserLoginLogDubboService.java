package com.niiwoo.shield.manage.sys.service;

import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogAddReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogQueryReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysLoginLogListRespDTO;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;

import java.util.List;

public interface UserLoginLogDubboService {
    /**
     * 插入login log
     *
     * @param sysLoginLogAddReqDTO
     * @return
     */
    Integer insertSysLoginLog(SysLoginLogAddReqDTO sysLoginLogAddReqDTO);

    /**
     * 分页查询login log
     *
     * @param sysLoginLogQueryReqDTO
     * @return
     */
    Page<SysLoginLogListRespDTO> queryLoginLogPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO);

    /**
     * 查询login log 列表
     *
     * @param sysLoginLogQueryReqDTO
     * @return
     */
    List<SysLoginLogListRespDTO> queryLoginLogListByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO);

    /**
     * 查询总页数
     *
     * @param sysLoginLogQueryReqDTO
     * @return
     */
    Integer queryLoginLogTotalPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO);
}
