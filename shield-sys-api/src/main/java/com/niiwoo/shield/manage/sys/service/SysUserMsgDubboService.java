package com.niiwoo.shield.manage.sys.service;



import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgDetailReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgListRespDTO;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;

import java.util.List;

public interface SysUserMsgDubboService {
    /**
     * 批量删除系统通知
     * @param idList
     * @return
     */
    Boolean deleteSysUserMsgByPrimaryKey(List<Long> idList);

    /**
     * 更新系统通知的状态
     * @param idList
     * @return
     */
    Boolean updateSysUserMsgReadStatusByPrimaryKey(List<Long> idList);

    /**
     * 查询系统通知列表
     * @param sysUserMsgListReqDTO
     * @return
     */
    PageResponseDTO<SysUserMsgListRespDTO> querySysUserMsgPageByCondition(SysUserMsgListReqDTO sysUserMsgListReqDTO);

    /**
     * 查询系统通知详情
     * @param sysUserMsgDetailReqDTO
     * @return
     */
    SysUserMsgDetailRespDTO querySysUserMsgDetail(SysUserMsgDetailReqDTO sysUserMsgDetailReqDTO);

    /**
     * 查询未读的系统通知
     * @param userId
     * @return
     */
    Long queryUnreadUserSysMsgCount(Long userId);
}
