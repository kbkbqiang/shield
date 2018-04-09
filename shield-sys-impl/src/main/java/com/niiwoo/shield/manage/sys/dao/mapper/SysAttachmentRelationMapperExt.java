package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dao.entity.SysAttachment;
import com.niiwoo.shield.manage.sys.dao.entity.SysAttachmentRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAttachmentRelationMapperExt extends SysAttachmentRelationMapper {
    int insertBatch(List<SysAttachmentRelation> attachmentRelationList);

    int deleteByMsgId(@Param("relationId") Long relationId, @Param("relationType") Byte relationType);

    List<SysAttachment> selectAttachListByMsgId(@Param("relationId") Long relationId, @Param("relationType") Byte relationType);
}