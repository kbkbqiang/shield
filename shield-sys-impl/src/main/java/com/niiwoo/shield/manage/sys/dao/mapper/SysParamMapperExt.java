package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysParam;

public interface SysParamMapperExt extends SysParamMapper {
    /**
     * 根据paramKey查询参数
     * @param paramKey
     * @return
     */
    SysParam selectByParamKey(String paramKey);
}