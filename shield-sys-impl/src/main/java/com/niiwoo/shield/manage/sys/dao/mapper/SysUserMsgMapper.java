package com.niiwoo.shield.manage.sys.dao.mapper;


import com.niiwoo.shield.manage.sys.dao.entity.SysUserMsg;

public interface SysUserMsgMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserMsg record);

    int insertSelective(SysUserMsg record);

    SysUserMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserMsg record);

    int updateByPrimaryKey(SysUserMsg record);

}