package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dao.entity.SysMsgDepartmentRelation;
import com.niiwoo.shield.manage.sys.dao.entity.SysUserMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMsgDepartmentRelationMapperExt extends SysMsgDepartmentRelationMapper {
    int insertBatch(List<SysMsgDepartmentRelation> msgDepartmentRelation);

    List<SysUserMsg> selectSendList(@Param(value = "msgId") Long msgId);

    int deleteByMsgId(@Param("msgId") Long msgId);

    List<Long> selectDepartmentListByMsgId(@Param(value = "msgId") Long msgId);
}