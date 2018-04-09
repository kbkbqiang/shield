package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysSmsTemplate;

public interface SysSmsTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSmsTemplate record);

    int insertSelective(SysSmsTemplate record);

    SysSmsTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysSmsTemplate record);

    int updateByPrimaryKey(SysSmsTemplate record);
}