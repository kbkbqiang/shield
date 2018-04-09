package com.niiwoo.shield.manage.sys.service;


import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgAddReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgDeleteReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgModifyReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgListRespDTO;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;

public interface SysMsgDubboService {
    /**
     * 查询后台通知列表
     * @param sysMsgListReqDTO
     * @return
     */
    PageResponseDTO<SysMsgListRespDTO> querySysMsgPageByCondition(SysMsgListReqDTO sysMsgListReqDTO);

    /**
     * 查询后台通知详情
     * @param msgId
     * @return
     */
    SysMsgDetailRespDTO querySysMsgByPrimaryKey(Long msgId);

    /**
     * 添加后台通知
     * @param sysMsgAddReqDTO
     * @return
     */
    Boolean addSysMsg(SysMsgAddReqDTO sysMsgAddReqDTO);

    /**
     * 修改后台通知
     * @param sysMsgModifyReqDTO
     * @return
     */
    Boolean updateSysMsg(SysMsgModifyReqDTO sysMsgModifyReqDTO);

    /**
     * 批量删除后台通知
     * @param sysMsgDeleteReqDTO
     * @return
     */
    Boolean deleteBatchSysMsgByPrimaryKey(SysMsgDeleteReqDTO sysMsgDeleteReqDTO);

}
