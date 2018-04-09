package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysMsgDepartmentRelation;
import org.apache.ibatis.annotations.Param;

public interface SysMsgDepartmentRelationMapper {
    int deleteByPrimaryKey(@Param("msgId") Long msgId, @Param("departmentId") Long departmentId);

    int insert(SysMsgDepartmentRelation record);

    int insertSelective(SysMsgDepartmentRelation record);

    SysMsgDepartmentRelation selectByPrimaryKey(@Param("msgId") Long msgId, @Param("departmentId") Long departmentId);

    int updateByPrimaryKeySelective(SysMsgDepartmentRelation record);

    int updateByPrimaryKey(SysMsgDepartmentRelation record);
}