package com.niiwoo.shield.manage.sys.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class SysMsg implements Serializable {
    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息类型：1-系统通知
     */
    private Byte msgType;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送状态：0-未发送，1-已发送
     */
    private Byte sendStatus;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    private Byte deleteFlag;

    /**
     * 创建用户ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新用户ID
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 消息内容
     */
    private String msgContent;

    private static final long serialVersionUID = 1L;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public Byte getMsgType() {
        return msgType;
    }

    public void setMsgType(Byte msgType) {
        this.msgType = msgType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Byte sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}