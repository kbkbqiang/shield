package com.niiwoo.shield.manage.sys.service.local;

import com.alibaba.fastjson.JSON;

import com.niiwoo.shield.manage.sys.dao.entity.SysParam;
import com.niiwoo.shield.manage.sys.dao.mapper.SysParamMapperExt;
import com.niiwoo.shield.manage.sys.dto.SysParamDTO;
import com.niiwoo.shield.manage.sys.enums.DelStatusEnum;
import com.niiwoo.shield.manage.sys.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysParamLocalService {
    private final String SYS_CONF_MAP_KEY = "sys_conf_map_key";

    @Autowired
    private SysParamMapperExt sysParamMapperExt;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public SysParamDTO queryByParamKey(String paramKey) {
        String jsonStr = (String) stringRedisTemplate.boundHashOps(SYS_CONF_MAP_KEY).get(paramKey);
        if (!StringUtils.isEmpty(jsonStr)) {
            return JSON.parseObject(jsonStr, SysParamDTO.class);
        }
        SysParam param = sysParamMapperExt.selectByParamKey(paramKey);
        if (param == null || DelStatusEnum.DEL.getValue().equals(param.getDelFlag())) {
            return null;
        }
        stringRedisTemplate.boundHashOps(SYS_CONF_MAP_KEY).put(paramKey, JSON.toJSONString(param));
        return ConvertUtils.convert(param, SysParamDTO.class);
    }
}
