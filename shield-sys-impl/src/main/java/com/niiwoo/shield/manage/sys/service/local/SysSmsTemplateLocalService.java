package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.sys.dao.entity.SysSmsTemplate;
import com.niiwoo.shield.manage.sys.dao.mapper.SysSmsTemplateMapperExt;
import com.niiwoo.shield.manage.sys.dto.SysSmsTemplateDTO;
import com.niiwoo.shield.manage.sys.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysSmsTemplateLocalService {
    @Autowired
    private SysSmsTemplateMapperExt sysSmsTemplateMapperExt;

    /**
     * 根据 templateCode 查询模板
     *
     * @param templateCode
     * @return
     */
    public SysSmsTemplateDTO queryByTemplateCode(String templateCode) {
        SysSmsTemplate smsTemplate = sysSmsTemplateMapperExt.selectByTemplateCode(templateCode);
        return ConvertUtils.convert(smsTemplate, SysSmsTemplateDTO.class);
    }
}
