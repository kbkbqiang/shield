package com.niiwoo.shield.manage.sys.service.dubbo;


import com.alibaba.dubbo.config.annotation.Service;

import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgAddReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgDeleteReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysMsgModifyReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgListRespDTO;
import com.niiwoo.shield.manage.sys.service.SysMsgDubboService;
import com.niiwoo.shield.manage.sys.service.local.SysMsgLocalService;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

@Service(version = "1.0.0", validation = "true")
public class SysMsgDubboServiceImpl implements SysMsgDubboService {

    @Autowired
    private SysMsgLocalService sysMsgLocalService;

    @Override
    public PageResponseDTO<SysMsgListRespDTO> querySysMsgPageByCondition(SysMsgListReqDTO sysMsgListReqDTO) {
        Assert.notNull(sysMsgListReqDTO, "SysMsgListRespDTO不能为空");
        return sysMsgLocalService.querySysMsgPageByCondition(sysMsgListReqDTO);
    }

    @Override
    public SysMsgDetailRespDTO querySysMsgByPrimaryKey(@NotNull Long msgId) {
        Assert.notNull(msgId, "后台通知ID不能为空");
        return sysMsgLocalService.querySysMsgByPrimaryKey(msgId);
    }

    @Override
    public Boolean addSysMsg(SysMsgAddReqDTO sysMsgAddReqDTO) {
        Assert.notNull(sysMsgAddReqDTO, "SysMsgAddReqDTO不能为空");
        Assert.notNull(sysMsgAddReqDTO.getSendTime(), "发送时间不能为空");
        Assert.hasText(sysMsgAddReqDTO.getMsgTitle(),"标题不能为空");
        Assert.hasText(sysMsgAddReqDTO.getMsgContent(),"通知内容不能为空");
        Assert.notNull(sysMsgAddReqDTO.getUserId(),"用户ID不能为空");
        Assert.notEmpty(sysMsgAddReqDTO.getDepartmentIdList(),"部门ID列表不能为空");
        return sysMsgLocalService.addSysMsg(sysMsgAddReqDTO);
    }

    @Override
    public Boolean updateSysMsg(SysMsgModifyReqDTO sysMsgModifyReqDTO) {
        Assert.notNull(sysMsgModifyReqDTO, "SysMsgModifyReqDTO不能为空");
        Assert.notNull(sysMsgModifyReqDTO.getMsgId(), "后台通知ID不能为空");
        Assert.notNull(sysMsgModifyReqDTO.getSendTime(), "发送时间不能为空");
        Assert.hasText(sysMsgModifyReqDTO.getMsgTitle(),"标题不能为空");
        Assert.hasText(sysMsgModifyReqDTO.getMsgContent(),"通知内容不能为空");
        Assert.notNull(sysMsgModifyReqDTO.getUserId(),"用户ID不能为空");
        Assert.notEmpty(sysMsgModifyReqDTO.getDepartmentIdList(),"部门ID列表不能为空");
        return sysMsgLocalService.updateSysMsg(sysMsgModifyReqDTO);
    }

    @Override
    public Boolean deleteBatchSysMsgByPrimaryKey(SysMsgDeleteReqDTO sysMsgDeleteReqDTO) {
        Assert.notNull(sysMsgDeleteReqDTO, "SysMsgDeleteReqDTO不能为空");
        Assert.notNull(sysMsgDeleteReqDTO.getUserId(),"用户ID不能为空");
        Assert.notEmpty(sysMsgDeleteReqDTO.getMsgIdList(),"后台通知ID列表不能为空");
        return sysMsgLocalService.deleteBatchSysMsgByPrimaryKey(sysMsgDeleteReqDTO);
    }
}
