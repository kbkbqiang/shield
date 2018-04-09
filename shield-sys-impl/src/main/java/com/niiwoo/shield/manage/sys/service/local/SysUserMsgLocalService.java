package com.niiwoo.shield.manage.sys.service.local;


import com.niiwoo.shield.manage.sys.dao.entity.SysUserMsg;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserMsgMapperExt;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgDetailReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgListRespDTO;
import com.niiwoo.shield.manage.sys.enums.ReadStatusEnum;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserMsgLocalService {

    @Autowired
    private SysUserMsgMapperExt sysUserMsgMapperExt;

    @Transactional
    public Boolean deleteSysUserMsgByPrimaryKey(List<Long> idList) {
        for (Long id : idList) {
            int result = sysUserMsgMapperExt.deleteByPrimaryKey(id);
            if (result != 1) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Boolean updateSysUserMsgReadStatusByPrimaryKey(List<Long> idList) {
        SysUserMsg updateUserMsg = new SysUserMsg();
        updateUserMsg.setReadStatus(ReadStatusEnum.READ.getCode());
        for (Long id : idList) {
            updateUserMsg.setId(id);
            int result = sysUserMsgMapperExt.updateByPrimaryKeySelective(updateUserMsg);
            if (result != 1) {
                return false;
            }
        }
        return true;
    }

    public PageResponseDTO<SysUserMsgListRespDTO> querySysUserMsgPageByCondition(SysUserMsgListReqDTO sysUserMsgListReqDTO) {
        PageResponseDTO<SysUserMsgListRespDTO> pageResponseDTO = new PageResponseDTO<>();
        long count = sysUserMsgMapperExt.selectSysUserMsgCount(sysUserMsgListReqDTO);
        pageResponseDTO.setTotalCount((int) count);
        pageResponseDTO.measureTotalPage(pageResponseDTO.getTotalCount(), sysUserMsgListReqDTO.getPageSize());
        if (sysUserMsgListReqDTO.getOffset() < count) {
            List<SysUserMsgListRespDTO> sysUserMsgListRespDTOS = sysUserMsgMapperExt.selectSysUserMsgPageByCondition(sysUserMsgListReqDTO);
            pageResponseDTO.setItems(sysUserMsgListRespDTOS);
        }
        return pageResponseDTO;
    }

    @Transactional
    public SysUserMsgDetailRespDTO querySysUserMsgDetail(SysUserMsgDetailReqDTO sysUserMsgDetailReqDTO) {
        SysUserMsgDetailRespDTO sysUserMsgDetailRespDTO = sysUserMsgMapperExt.selectSysUserMsgDetail(sysUserMsgDetailReqDTO);
        if (sysUserMsgDetailRespDTO != null) {
            //更新为已读状态
            SysUserMsg updateUserMsg = new SysUserMsg();
            updateUserMsg.setReadStatus(ReadStatusEnum.READ.getCode());
            updateUserMsg.setId(sysUserMsgDetailRespDTO.getId());
            sysUserMsgMapperExt.updateByPrimaryKeySelective(updateUserMsg);
        }
        return sysUserMsgDetailRespDTO;
    }

    public Long queryUnreadUserSysMsgCount(Long userId) {
        SysUserMsgListReqDTO sysUserMsgListReqDTO = new SysUserMsgListReqDTO();
        sysUserMsgListReqDTO.setUserId(userId);
        sysUserMsgListReqDTO.setReadStatus(ReadStatusEnum.NOT_READ.getCode());
        return sysUserMsgMapperExt.selectSysUserMsgCount(sysUserMsgListReqDTO);
    }
}
