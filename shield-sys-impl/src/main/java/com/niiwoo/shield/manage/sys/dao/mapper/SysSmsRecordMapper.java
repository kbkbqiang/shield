package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysSmsRecord;

public interface SysSmsRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysSmsRecord record);

    int insertSelective(SysSmsRecord record);

    SysSmsRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysSmsRecord record);

    int updateByPrimaryKey(SysSmsRecord record);
}