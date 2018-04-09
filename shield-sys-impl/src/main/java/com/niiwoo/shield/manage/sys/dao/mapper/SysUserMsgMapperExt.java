package com.niiwoo.shield.manage.sys.dao.mapper;



import com.niiwoo.shield.manage.sys.dao.entity.SysUserMsg;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgDetailReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgListRespDTO;

import java.util.List;
import java.util.Map;

public interface SysUserMsgMapperExt extends SysUserMsgMapper{

    int updateBatchByPrimaryKeySelective(Map<String, Object> map);

    int deleteBatchByPrimaryKey(List<Long> ids);

    int deleteByMsgId(Long msgId);

    int insertBatch(List<SysUserMsg> msgList);

    List<SysUserMsgListRespDTO> selectSysUserMsgPageByCondition(SysUserMsgListReqDTO sysUserMsgListReqDTO);

    Long selectSysUserMsgCount(SysUserMsgListReqDTO sysUserMsgListReqDTO);

    SysUserMsgDetailRespDTO selectSysUserMsgDetail(SysUserMsgDetailReqDTO sysUserMsgDetailReqDTO);
}