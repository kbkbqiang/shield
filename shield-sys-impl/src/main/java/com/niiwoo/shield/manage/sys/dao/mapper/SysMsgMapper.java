package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dao.entity.SysMsg;

public interface SysMsgMapper {

    int deleteByPrimaryKey(Long msgid);

    int insert(SysMsg record);

    int insertSelective(SysMsg record);

    SysMsg selectByPrimaryKey(Long msgid);

    int updateByPrimaryKeySelective(SysMsg record);

    int updateByPrimaryKey(SysMsg record);

}