package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.niiwoo.shield.manage.sys.dto.msg.request.*;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgListRespDTO;
import com.niiwoo.shield.manage.sys.service.SysMsgDubboService;
import com.niiwoo.shield.manage.web.util.CommonUtil;
import com.niiwoo.shield.manage.web.util.ConvertUtils;
import com.niiwoo.shield.manage.web.vo.msg.request.*;
import com.niiwoo.shield.manage.web.vo.msg.response.AttachmentRespVO;
import com.niiwoo.shield.manage.web.vo.msg.response.SysMessageDetailRespVO;
import com.niiwoo.shield.manage.web.vo.msg.response.SysMessageListRespVO;
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
@RequestMapping("/sysmsg")
@Api(tags = "后台通知管理(吴越)", description = "/sysmsg")
public class SysMessageManageController {

    @Reference(version = "1.0.0")
    private SysMsgDubboService sysMsgDubboService;

    @PostMapping("/queryMsgList")
    @ApiOperation("后台通知列表")
    public Result<PageResponseVO<SysMessageListRespVO>> queryMsgList(@RequestBody @ApiParam("后台通知查询信息") SysMessageListQueryReqVO vo) {
        PageResponseDTO<SysMsgListRespDTO> pageResponseDTO = sysMsgDubboService.querySysMsgPageByCondition(ConvertUtils.convert(vo, SysMsgListReqDTO.class));
        return Result.with(ConvertUtils.convertPage(pageResponseDTO, SysMessageListRespVO.class));
    }

    @PostMapping("/queryMsgDetail")
    @ApiOperation("后台通知详情")
    public Result<SysMessageDetailRespVO> queryMsgDetail(@RequestBody @Validated @ApiParam("后台通知ID") SysMessageDetailQueryReqVO vo) {
        SysMsgDetailRespDTO sysMsgDetailRespDTO = sysMsgDubboService.querySysMsgByPrimaryKey(vo.getMsgId());
        SysMessageDetailRespVO sysMessageDetailRespVO = ConvertUtils.convert(sysMsgDetailRespDTO, SysMessageDetailRespVO.class);
        List<AttachmentRespVO> attachmentRespVOList = ConvertUtils.convert(sysMsgDetailRespDTO.getAttachmentRespDTOList(), AttachmentRespVO.class);
        sysMessageDetailRespVO.setAttachmentList(attachmentRespVOList);
        return Result.with(sysMessageDetailRespVO);
    }

    @PostMapping("/addMsg")
    @ApiOperation("新增后台通知")
    public Result addMsg(@RequestBody @Validated @ApiParam("后台通知信息") SysMessageAddReqVO vo) {
        SysMsgAddReqDTO sysMsgAddReqDTO = new SysMsgAddReqDTO();
        sysMsgAddReqDTO.setMsgTitle(vo.getMsgTitle());
        sysMsgAddReqDTO.setMsgContent(vo.getMsgContent());
        sysMsgAddReqDTO.setSendTime(vo.getSendTime());
        sysMsgAddReqDTO.setUserId(CommonUtil.getCurrentUserId());
        sysMsgAddReqDTO.setDepartmentIdList(vo.getDepartmentIdList());
        sysMsgAddReqDTO.setAttachmentReqDTOList(ConvertUtils.convert(vo.getAttachmentList(), AttachmentReqDTO.class));
        Boolean result = sysMsgDubboService.addSysMsg(sysMsgAddReqDTO);
        return Result.with(result);
    }

    @PostMapping("/updateMsg")
    @ApiOperation("更新后台通知")
    public Result updateMsg(@RequestBody @Validated @ApiParam("后台通知信息") SysMessageModifyReqVO vo) {
        SysMsgModifyReqDTO sysMsgModifyReqDTO = new SysMsgModifyReqDTO();
        sysMsgModifyReqDTO.setMsgId(vo.getMsgId());
        sysMsgModifyReqDTO.setMsgTitle(vo.getMsgTitle());
        sysMsgModifyReqDTO.setMsgContent(vo.getMsgContent());
        sysMsgModifyReqDTO.setSendTime(vo.getSendTime());
        sysMsgModifyReqDTO.setUserId(CommonUtil.getCurrentUserId());
        sysMsgModifyReqDTO.setDepartmentIdList(vo.getDepartmentIdList());
        sysMsgModifyReqDTO.setAttachmentReqDTOList(ConvertUtils.convert(vo.getAttachmentList(), AttachmentReqDTO.class));
        Boolean result = sysMsgDubboService.updateSysMsg(sysMsgModifyReqDTO);
        return Result.with(result);
    }

    @PostMapping("/deleteMsg")
    @ApiOperation("删除后台通知")
    public Result deleteMsg(@RequestBody @Validated @ApiParam("后台通知ID列表") SysMessageDelReqVO vo) {
        SysMsgDeleteReqDTO sysMsgDeleteReqDTO = new SysMsgDeleteReqDTO();
        sysMsgDeleteReqDTO.setMsgIdList(vo.getMsgIdList());
        sysMsgDeleteReqDTO.setUserId(CommonUtil.getCurrentUserId());
        Boolean result = sysMsgDubboService.deleteBatchSysMsgByPrimaryKey(sysMsgDeleteReqDTO);
        return Result.with(result);
    }


}
