package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgDetailReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.request.SysUserMsgListReqDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysUserMsgListRespDTO;
import com.niiwoo.shield.manage.sys.service.SysUserMsgDubboService;
import com.niiwoo.shield.manage.web.util.CommonUtil;
import com.niiwoo.shield.manage.web.util.ConvertUtils;
import com.niiwoo.shield.manage.web.vo.msg.request.SysMessageDetailQueryReqVO;
import com.niiwoo.shield.manage.web.vo.msg.request.SysUserMessageListQueryReqVO;
import com.niiwoo.shield.manage.web.vo.msg.request.SysUserMessageUpdateReqVO;
import com.niiwoo.shield.manage.web.vo.msg.response.AttachmentRespVO;
import com.niiwoo.shield.manage.web.vo.msg.response.SysUserMessageDetailRespVO;
import com.niiwoo.shield.manage.web.vo.msg.response.SysUserMessageListRespVO;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import com.niiwoo.tripod.web.vo.PageResponseVO;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sysuser-msg")
@Api(tags = "个人管理-系统通知(吴越)", description = "/sysuser-msg")
public class SysUserMessageManageController {

    @Reference(version = "1.0.0")
    private SysUserMsgDubboService sysUserMsgDubboService;

    @PostMapping("/querySysUserMsgList")
    @ApiOperation("系统通知列表")
    public Result<PageResponseVO<SysUserMessageListRespVO>> querySysUserMsgList(@RequestBody @ApiParam("系统通知查询信息") SysUserMessageListQueryReqVO vo) {
        SysUserMsgListReqDTO sysUserMsgListReqDTO = ConvertUtils.convert(vo, SysUserMsgListReqDTO.class);
        sysUserMsgListReqDTO.setUserId(CommonUtil.getCurrentUserId());
        PageResponseDTO<SysUserMsgListRespDTO> pageResponseDTO = sysUserMsgDubboService.querySysUserMsgPageByCondition(sysUserMsgListReqDTO);
        return Result.with(ConvertUtils.convertPage(pageResponseDTO, SysUserMessageListRespVO.class));
    }


    @PostMapping("/updateSysUserMsgReadStatus")
    @ApiOperation("批量设置系统通知已读")
    public Result updateSysUserMsgReadStatus(@RequestBody @Validated @ApiParam("系统通知ID列表") SysUserMessageUpdateReqVO vo) {
        Boolean result = sysUserMsgDubboService.updateSysUserMsgReadStatusByPrimaryKey(vo.getIdList());
        return Result.with(result);
    }

    @PostMapping("/deleteSysUserMsg")
    @ApiOperation("批量删除系统通知")
    public Result deleteSysUserMsg(@RequestBody @Validated @ApiParam("系统通知ID列表") SysUserMessageUpdateReqVO vo) {
        Boolean result = sysUserMsgDubboService.deleteSysUserMsgByPrimaryKey(vo.getIdList());
        return Result.with(result);
    }

    @PostMapping("/querySysUserMsgDetail")
    @ApiOperation("查询系统通知详情")
    public Result<SysUserMessageDetailRespVO> querySysUserMsgDetail(@RequestBody @Validated @ApiParam("后台通知ID") SysMessageDetailQueryReqVO vo) {
        SysUserMsgDetailReqDTO sysUserMsgDetailReqDTO = new SysUserMsgDetailReqDTO();
        sysUserMsgDetailReqDTO.setUserId(CommonUtil.getCurrentUserId());
        sysUserMsgDetailReqDTO.setMsgId(vo.getMsgId());
        SysUserMsgDetailRespDTO sysUserMsgDetailRespDTO = sysUserMsgDubboService.querySysUserMsgDetail(sysUserMsgDetailReqDTO);
        SysUserMessageDetailRespVO sysUserMessageDetailRespVO = ConvertUtils.convert(sysUserMsgDetailRespDTO, SysUserMessageDetailRespVO.class);
        List<AttachmentRespVO> attachmentRespVOList = ConvertUtils.convert(sysUserMsgDetailRespDTO.getAttachmentRespDTOList(), AttachmentRespVO.class);
        sysUserMessageDetailRespVO.setAttachmentList(attachmentRespVOList);
        return Result.with(sysUserMessageDetailRespVO);
    }

    @PostMapping("/queryUnreadUserSysMsgCount")
    @ApiOperation("查询未读系统通知的条数")
    public Result<Long> queryUnreadUserSysMsgCount() {
        return Result.with(sysUserMsgDubboService.queryUnreadUserSysMsgCount(CommonUtil.getCurrentUserId()));
    }

}
