package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogQueryReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysLoginLogListRespDTO;

import java.util.List;

public interface SysLoginLogMapperExt extends SysLoginLogMapper {
    Long selectLoginLogCountByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO);

    List<SysLoginLogListRespDTO> selectLoginLogListByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO);

}