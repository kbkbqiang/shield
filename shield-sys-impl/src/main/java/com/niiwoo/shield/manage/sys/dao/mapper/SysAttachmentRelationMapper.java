package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysAttachmentRelation;
import org.apache.ibatis.annotations.Param;

public interface SysAttachmentRelationMapper {
    int deleteByPrimaryKey(@Param("relationId") Long relationId, @Param("attachmentId") Long attachmentId, @Param("relationType") Byte relationType);

    int insert(SysAttachmentRelation record);

    int insertSelective(SysAttachmentRelation record);

    SysAttachmentRelation selectByPrimaryKey(@Param("relationId") Long relationId, @Param("attachmentId") Long attachmentId, @Param("relationType") Byte relationType);

    int updateByPrimaryKeySelective(SysAttachmentRelation record);

    int updateByPrimaryKey(SysAttachmentRelation record);
}