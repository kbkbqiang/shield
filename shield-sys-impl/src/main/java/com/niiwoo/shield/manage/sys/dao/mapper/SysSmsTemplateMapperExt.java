package com.niiwoo.shield.manage.sys.dao.mapper;

import com.niiwoo.shield.manage.sys.dao.entity.SysSmsTemplate;

public interface SysSmsTemplateMapperExt extends SysSmsTemplateMapper {
    /**
     * 根据 templateCode 查询模板
     *
     * @param templateCode
     * @return
     */
    SysSmsTemplate selectByTemplateCode(String templateCode);
}