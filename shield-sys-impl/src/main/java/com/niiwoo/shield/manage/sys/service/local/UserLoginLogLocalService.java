package com.niiwoo.shield.manage.sys.service.local;


import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dao.entity.SysLoginLog;
import com.niiwoo.shield.manage.sys.dao.mapper.SysLoginLogMapperExt;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogAddReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogQueryReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysLoginLogListRespDTO;
import com.niiwoo.shield.manage.sys.util.ConvertUtils;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

/**
 * @author zhuhecheng
 * @date 2017-06-13
 */
@Service
public class UserLoginLogLocalService {

    @Autowired
    private SysLoginLogMapperExt sysLoginLogMapperExt;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Transactional
    public int insertSysLoginLog(SysLoginLogAddReqDTO sysLoginLogAddReqDTO) {
        Long id = snowflakeIdWorker.nextId();
        SysLoginLog sysLoginLog = ConvertUtils.convert(sysLoginLogAddReqDTO, SysLoginLog.class);
        sysLoginLog.setId(id);
        return sysLoginLogMapperExt.insertSelective(sysLoginLog);
    }

    public Page<SysLoginLogListRespDTO> queryLoginLogPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        Page<SysLoginLogListRespDTO> pageResponseDTO = new Page<SysLoginLogListRespDTO>();
        long totalRecord = sysLoginLogMapperExt.selectLoginLogCountByCondition(sysLoginLogQueryReqDTO);
        pageResponseDTO.setTotalRecord((int) totalRecord);
        if (totalRecord > sysLoginLogQueryReqDTO.getOffset()) {
            List<SysLoginLogListRespDTO> sysLoginLogListRespDTOS = sysLoginLogMapperExt.selectLoginLogListByCondition(sysLoginLogQueryReqDTO);
            pageResponseDTO.setResult(sysLoginLogListRespDTOS);
        }
        return pageResponseDTO;
    }

    public List<SysLoginLogListRespDTO> queryLoginLogListByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        return sysLoginLogMapperExt.selectLoginLogListByCondition(sysLoginLogQueryReqDTO);
    }

    public Integer queryLoginLogTotalPageByCondition(SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO) {
        long totalRecord = sysLoginLogMapperExt.selectLoginLogCountByCondition(sysLoginLogQueryReqDTO);
        return (int) (totalRecord + sysLoginLogQueryReqDTO.getPageSize() - 1) / sysLoginLogQueryReqDTO.getPageSize();
    }

}
