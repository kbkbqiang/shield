package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysAttachment;

public interface SysAttachmentMapper {
    int deleteByPrimaryKey(Long attachmentId);

    int insert(SysAttachment record);

    int insertSelective(SysAttachment record);

    SysAttachment selectByPrimaryKey(Long attachmentId);

    int updateByPrimaryKeySelective(SysAttachment record);

    int updateByPrimaryKey(SysAttachment record);
}