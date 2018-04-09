package com.niiwoo.shield.manage.sys.service.local;


import com.niiwoo.shield.manage.sys.dao.entity.*;
import com.niiwoo.shield.manage.sys.dao.mapper.*;
import com.niiwoo.shield.manage.sys.dto.msg.request.*;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgDetailRespDTO;
import com.niiwoo.shield.manage.sys.dto.msg.response.SysMsgListRespDTO;
import com.niiwoo.shield.manage.sys.enums.*;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMsgLocalService {

    @Autowired
    private SysMsgMapperExt sysMsgMapperExt;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private SysAttachmentRelationMapperExt sysAttachmentRelationMapperExt;

    @Autowired
    private SysMsgDepartmentRelationMapperExt sysMsgDepartmentRelationMapperExt;

    @Autowired
    private SysUserMsgMapperExt sysUserMsgMapperExt;

    @Autowired
    private SysAttachmentMapperExt sysAttachmentMapperExt;

    @Autowired
    private SysUserMapperExt sysUserMapperExt;

    public PageResponseDTO<SysMsgListRespDTO> querySysMsgPageByCondition(SysMsgListReqDTO sysMsgListReqDTO) {
        PageResponseDTO<SysMsgListRespDTO> pageResponseDTO = new PageResponseDTO<>();
        if (sysMsgListReqDTO.getMsgTitle() != null) {
            sysMsgListReqDTO.setMsgTitle(sysMsgListReqDTO.getMsgTitle().trim());
        }
        long count = sysMsgMapperExt.selectSysMsgCount(sysMsgListReqDTO);
        pageResponseDTO.setTotalCount((int) count);
        pageResponseDTO.measureTotalPage(pageResponseDTO.getTotalCount(), sysMsgListReqDTO.getPageSize());
        if (sysMsgListReqDTO.getOffset() < count) {
            List<SysMsgListRespDTO> sysMsgListRespDTOS = sysMsgMapperExt.selectSysMsgPageByCondition(sysMsgListReqDTO);
            pageResponseDTO.setItems(sysMsgListRespDTOS);
        }
        return pageResponseDTO;
    }

    public SysMsgDetailRespDTO querySysMsgByPrimaryKey(Long msgId) {
        SysMsgDetailRespDTO sysMsgDetailRespDTO = sysMsgMapperExt.selectSysMsgDetailByPrimaryKey(msgId);
//        if (sysMsgDetailRespDTO != null) {
//            List<SysAttachment> attachmentList = sysAttachmentRelationMapperExt.selectAttachListByMsgId(msgId, RelationTypeEnum.RELATIONTYPE_MSG.getCode());
//            sysMsgDetailRespDTO.setAttachmentList(ConvertUtils.convert(attachmentList, AttachmentRespDTO.class));
//            List<Long> departmentIdList = sysMsgDepartmentRelationMapperExt.selectDepartmentListByMsgId(msgId);
//            sysMsgDetailRespDTO.setDepartmentIdList(departmentIdList);
//        }
        return sysMsgDetailRespDTO;
    }

    @Transactional
    public Boolean addSysMsg(SysMsgAddReqDTO sysMsgAddReqDTO) {

        Long msgId = snowflakeIdWorker.nextId();
        SysMsg msg = new SysMsg();
        msg.setMsgId(msgId);
        msg.setMsgTitle(sysMsgAddReqDTO.getMsgTitle());
        msg.setSendTime(sysMsgAddReqDTO.getSendTime());
        msg.setMsgContent(sysMsgAddReqDTO.getMsgContent());
        msg.setDeleteFlag(DeleteFlagEnum.NOT_DELETE.getValue());
        msg.setSendStatus(SendStatusEnum.NOT_SEND.getCode());
        msg.setMsgType(MsgTypeEnum.SYSTEM_MSG.getMsgTypeCode());

        msg.setCreateTime(new Date());
        msg.setCreateUserId(sysMsgAddReqDTO.getUserId());
        msg.setUpdatedTime(msg.getCreateTime());
        msg.setUpdateUserId(msg.getCreateUserId());

        sysMsgMapperExt.insertSelective(msg);

        List<AttachmentReqDTO> attachList = sysMsgAddReqDTO.getAttachmentReqDTOList();
        addAttachmnetAndAttachmentRelation(attachList, sysMsgAddReqDTO.getUserId(), msgId);

        List<Long> departmentIdList = sysMsgAddReqDTO.getDepartmentIdList();
        addDepartmentRelation(departmentIdList, sysMsgAddReqDTO.getUserId(), msgId);
        //给user发送后台通知
        sendSysUserMsg(msgId, departmentIdList);
        return true;
    }

    @Transactional
    public Boolean updateSysMsg(SysMsgModifyReqDTO sysMsgModifyReqDTO) {
        Long msgId = sysMsgModifyReqDTO.getMsgId();
        SysMsg msg = sysMsgMapperExt.selectByPrimaryKey(msgId);
        if (msg != null) {
            msg.setMsgId(msgId);
            msg.setSendTime(sysMsgModifyReqDTO.getSendTime());
            msg.setMsgTitle(sysMsgModifyReqDTO.getMsgTitle());
            msg.setMsgContent(sysMsgModifyReqDTO.getMsgContent());
            msg.setUpdatedTime(new Date());
            msg.setUpdateUserId(sysMsgModifyReqDTO.getUserId());
            sysMsgMapperExt.updateByPrimaryKeySelective(msg);

            List<AttachmentReqDTO> attachmentList = sysMsgModifyReqDTO.getAttachmentReqDTOList();
            List<SysAttachment> queryAttachmentList = sysAttachmentRelationMapperExt.selectAttachListByMsgId(msgId, RelationTypeEnum.RELATIONTYPE_MSG.getCode());
            boolean attachmentNeedUpdate = false;
            if (attachmentList.size() == queryAttachmentList.size()) {
                for (AttachmentReqDTO attachmentAddReqDTO : attachmentList) {
                    int i = 0;
                    for (i = 0; i < queryAttachmentList.size(); i++) {
                        if (attachmentAddReqDTO.getAttachmentPath().equals(queryAttachmentList.get(i).getAttachmentPath())) {
                            break;
                        }
                    }
                    if (i == queryAttachmentList.size()) {
                        attachmentNeedUpdate = true;
                        break;
                    }
                }
            } else {
                attachmentNeedUpdate = true;
            }

            if (attachmentNeedUpdate) {
                //删除附件
                for (SysAttachment a : queryAttachmentList) {
                    sysAttachmentMapperExt.deleteByPrimaryKey(a.getAttachmentId());
                }
                //删除附件关系
                sysAttachmentRelationMapperExt.deleteByMsgId(msgId, RelationTypeEnum.RELATIONTYPE_MSG.getCode());
                addAttachmnetAndAttachmentRelation(attachmentList, sysMsgModifyReqDTO.getUserId(), msgId);
            }


            List<Long> departmentIdList = sysMsgModifyReqDTO.getDepartmentIdList();
            List<Long> queryDepartmentList = sysMsgDepartmentRelationMapperExt.selectDepartmentListByMsgId(msgId);
            boolean deparmentNeedUpdate = false;
            if (departmentIdList.size() == queryDepartmentList.size()) {
                for (Long deparmentId : departmentIdList) {
                    int i = 0;
                    for (i = 0; i < queryDepartmentList.size(); i++) {
                        if (deparmentId.longValue() == queryDepartmentList.get(i).longValue()) {
                            break;
                        }
                    }
                    if (i == departmentIdList.size()) {
                        deparmentNeedUpdate = true;
                        break;
                    }
                }
            } else {
                deparmentNeedUpdate = true;
            }
            if (deparmentNeedUpdate) {
                //删除部门和后台通知的关系
                sysMsgDepartmentRelationMapperExt.deleteByMsgId(msgId);
                addDepartmentRelation(departmentIdList, sysMsgModifyReqDTO.getUserId(), msgId);
                //删除个人系统消息
                sysUserMsgMapperExt.deleteByMsgId(msgId);
                //给user发送后台通知
                sendSysUserMsg(msgId, departmentIdList);
            }
        }
        return true;
    }

    @Transactional
    public Boolean deleteBatchSysMsgByPrimaryKey(SysMsgDeleteReqDTO sysMsgDeleteReqDTO) {
        SysMsg msg = new SysMsg();
        msg.setDeleteFlag(DeleteFlagEnum.DELETED.getValue());
        msg.setUpdateUserId(sysMsgDeleteReqDTO.getUserId());
        msg.setUpdatedTime(new Date());
        for (Long msgId : sysMsgDeleteReqDTO.getMsgIdList()) {
            msg.setMsgId(msgId);
            int result = sysMsgMapperExt.updateByPrimaryKeySelective(msg);
            if (result != 1) {
                return false;
            }
        }
        return true;
    }

    private SysAttachmentRelation createAttachmentRelation(Long relationId, Byte relationType, Long attachmentId, Long userId) {
        SysAttachmentRelation attachmentRelation = new SysAttachmentRelation();
        attachmentRelation.setAttachmentId(attachmentId);
        attachmentRelation.setRelationId(relationId);
        attachmentRelation.setRelationType(relationType);
        attachmentRelation.setCreateUserId(userId);
        attachmentRelation.setCreateTime(new Date());
        return attachmentRelation;
    }

    private SysMsgDepartmentRelation createDepartmentRelation(Long msgId, Long departmentId, Long userId) {
        SysMsgDepartmentRelation departmentRelation = new SysMsgDepartmentRelation();
        departmentRelation.setMsgId(msgId);
        departmentRelation.setDepartmentId(departmentId);
        departmentRelation.setCreateUserId(userId);
        departmentRelation.setCreateTime(new Date());
        return departmentRelation;
    }

    private SysAttachment createAttachment(String attachmentName, String attachmentPath, Long userId) {
        SysAttachment attachment = new SysAttachment();
        attachment.setAttachmentId(snowflakeIdWorker.nextId());
        attachment.setAttachmentName(attachmentName);
        attachment.setAttachmentPath(attachmentPath);
        attachment.setCreateUserId(userId);
        attachment.setCreateTime(new Date());
        return attachment;
    }

    private void sendSysUserMsg(Long msgId, List<Long> departmentIdList) {
        //如果发送对象为全部
        List<SysUserMsg> sysUserMsgList = null;
        if (departmentIdList != null && departmentIdList.size() == 1 && departmentIdList.get(0).longValue() == 0) {
            List<Long> userIdList = sysUserMapperExt.selectAllUserId();
            sysUserMsgList = new ArrayList<>();
            for (Long userId : userIdList) {
                SysUserMsg sysUserMsg = new SysUserMsg();
                sysUserMsg.setMsgId(msgId);
                sysUserMsg.setUserId(userId);
                sysUserMsgList.add(sysUserMsg);
            }
        } else {
            sysUserMsgList = sysMsgDepartmentRelationMapperExt.selectSendList(msgId);
        }
        if (sysUserMsgList != null && sysUserMsgList.size() > 0) {
            for (SysUserMsg userMsg : sysUserMsgList) {
                userMsg.setId(snowflakeIdWorker.nextId());
                userMsg.setCreateTime(new Date());
                userMsg.setUpdatedTime(new Date());
                userMsg.setReadStatus(ReadStatusEnum.NOT_READ.getCode());
                sysUserMsgMapperExt.insert(userMsg);
            }
        }

    }

    private void addAttachmnetAndAttachmentRelation(List<AttachmentReqDTO> attachList, Long userId, Long msgId) {
        if (attachList != null && attachList.size() > 0) {
            List<SysAttachment> sysAttachmentList = attachList.stream().
                    map(attachmentAddReqDTO -> this.createAttachment(attachmentAddReqDTO.getAttachmentName(), attachmentAddReqDTO.getAttachmentPath(), userId)).collect(Collectors.toList());

            for (SysAttachment sysAttachment : sysAttachmentList) {
                sysAttachmentMapperExt.insert(sysAttachment);
            }
            List<SysAttachmentRelation> attachmentRelationList = sysAttachmentList.stream().
                    map(attachment -> this.createAttachmentRelation(msgId, RelationTypeEnum.RELATIONTYPE_MSG.getCode(), attachment.getAttachmentId(), userId)).collect(Collectors.toList());
            for (SysAttachmentRelation sysAttachmentRelation : attachmentRelationList) {
                sysAttachmentRelationMapperExt.insert(sysAttachmentRelation);
            }

        }
    }

    private void addDepartmentRelation(List<Long> departmentIdList, Long userId, Long msgId) {
        if (departmentIdList != null && departmentIdList.size() > 0) {
            List<SysMsgDepartmentRelation> departmentRelationList = departmentIdList.stream().
                    map(departmentId -> this.createDepartmentRelation(msgId, departmentId, userId)).collect(Collectors.toList());
            for (SysMsgDepartmentRelation sysMsgDepartmentRelation : departmentRelationList) {
                sysMsgDepartmentRelationMapperExt.insert(sysMsgDepartmentRelation);
            }
        }
    }
}
