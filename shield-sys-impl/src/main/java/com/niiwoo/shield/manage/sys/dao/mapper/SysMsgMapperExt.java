package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgListRespDTO;

import java.util.List;
import java.util.Map;

public interface SysMsgMapperExt extends SysMsgMapper{
    List<SysMsgListRespDTO> selectSysMsgPageByCondition(SysMsgListReqDTO sysMsgListReqDTO);

    Long selectSysMsgCount(SysMsgListReqDTO sysMsgListReqDTO);

    int updateBatchByPrimaryKeySelective(Map<String, Object> map);

    SysMsgDetailRespDTO selectSysMsgDetailByPrimaryKey(Long msgId);

}