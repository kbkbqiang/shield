package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgDetailReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgListRespDTO;
import com.niiwoo.shield.manage.sys.service.SysUserMsgDubboService;
import com.niiwoo.shield.manage.sys.service.local.SysUserMsgLocalService;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service(version = "1.0.0", validation = "true")
public class SysUserMsgDubboServiceImpl implements SysUserMsgDubboService {

    @Autowired
    private SysUserMsgLocalService sysUserMsgLocalServie;

    @Override
    public Boolean deleteSysUserMsgByPrimaryKey(@NotEmpty List<Long> idList) {
        Assert.notEmpty(idList,"通知ID列表不能为空");
        return sysUserMsgLocalServie.deleteSysUserMsgByPrimaryKey(idList);
    }

    @Override
    public Boolean updateSysUserMsgReadStatusByPrimaryKey(@NotEmpty List<Long> idList) {
        Assert.notEmpty(idList,"通知ID列表不能为空");
        return sysUserMsgLocalServie.updateSysUserMsgReadStatusByPrimaryKey(idList);
    }

    @Override
    public PageResponseDTO<SysUserMsgListRespDTO> querySysUserMsgPageByCondition(SysUserMsgListReqDTO sysUserMsgListReqDTO) {
        Assert.notNull(sysUserMsgListReqDTO,"SysUserMsgListRespDTO不能为空");
        return sysUserMsgLocalServie.querySysUserMsgPageByCondition(sysUserMsgListReqDTO);
    }

    @Override
    public SysUserMsgDetailRespDTO querySysUserMsgDetail(SysUserMsgDetailReqDTO sysUserMsgDetailReqDTO) {
        Assert.notNull(sysUserMsgDetailReqDTO,"SysUserMsgDetailReqDTO不能为空");
        Assert.notNull(sysUserMsgDetailReqDTO.getUserId(),"用户ID不能为空");
        Assert.notNull(sysUserMsgDetailReqDTO.getMsgId(),"后台通知ID不能为空");
        return sysUserMsgLocalServie.querySysUserMsgDetail(sysUserMsgDetailReqDTO);
    }

    @Override
    public Long queryUnreadUserSysMsgCount(@NotNull Long userId) {
        Assert.notNull(userId,"用户ID不能为空");
        return sysUserMsgLocalServie.queryUnreadUserSysMsgCount(userId);
    }
}
