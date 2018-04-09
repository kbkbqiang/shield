package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dao.entity.SysAttachment;

import java.util.List;

public interface SysAttachmentMapperExt extends SysAttachmentMapper {
    int insertBatch(List<SysAttachment> attachmentList);

    int deleteBatchByPrimaryKey(List<Long> idList);
}