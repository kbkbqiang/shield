package com.niiwoo.shield.manage.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.sys.dto.user.request.SysLoginLogQueryReqDTO;
import com.niiwoo.shield.manage.sys.dto.user.response.SysLoginLogListRespDTO;
import com.niiwoo.shield.manage.sys.service.UserLoginLogDubboService;
import com.niiwoo.shield.manage.web.util.ConvertUtils;
import com.niiwoo.shield.manage.web.util.ExcelExport;
import com.niiwoo.shield.manage.web.vo.user.request.SysLoginLogExportReqVO;
import com.niiwoo.shield.manage.web.vo.user.request.SysLoginLogQueryReqVO;
import com.niiwoo.shield.manage.web.vo.user.response.SysLoginLogListRespVO;
import com.niiwoo.tripod.provider.dto.response.PageResponseDTO;
import com.niiwoo.tripod.web.vo.PageResponseVO;
import com.niiwoo.tripod.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuhecheng on 2017/8/12.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class LoginLogController {

    @Reference(version = "1.0.0")
    private UserLoginLogDubboService userLoginLogDubboService;

    /**
     * 分页查询用户登录日志
     *
     * @param vo
     * @return
     */
    @PostMapping("/queryLoginLogs")
    @RequiresPermissions({"user:queryLoginLogs"})
    public Result<Page<SysLoginLogListRespVO>> queryLoginLogs(@RequestBody SysLoginLogQueryReqVO vo) {
        Page<SysLoginLogListRespDTO> page = userLoginLogDubboService.queryLoginLogPageByCondition(ConvertUtils.convert(vo, SysLoginLogQueryReqDTO.class));
        Page<SysLoginLogListRespVO> resultPage = new Page<SysLoginLogListRespVO>();
        resultPage.setTotalRecord(page.getTotalRecord());
        List<SysLoginLogListRespDTO> list = page.getResult();
        if(list!=null && !list.isEmpty()){
            List<SysLoginLogListRespVO> resultList = ConvertUtils.convert(list, SysLoginLogListRespVO.class);
            resultPage.setResult(resultList);
        }

        return Result.with(resultPage);
    }

    /**
     * 分页查询用户登录日志
     *
     * @param vo
     * @param response
     * @return
     */
    @PostMapping("/exportLoginLogs")
    @RequiresPermissions({"user:exportLoginLogs"})
    public void exportLoginLogs(SysLoginLogExportReqVO vo, HttpServletResponse response) {
        String fileName = "登录IP记录";
        String[] titleNames = {"登录时间", "登录IP"};
        String[] columnNames = {"loginTime", "loginIp"};
        String[] columnTypes = {"Date", "string"};
        ExcelExport excelExport = new ExcelExport(response, fileName, titleNames, columnNames, columnTypes);
        SysLoginLogQueryReqDTO sysLoginLogQueryReqDTO = ConvertUtils.convert(vo, SysLoginLogQueryReqDTO.class);
        Integer totalPage = userLoginLogDubboService.queryLoginLogTotalPageByCondition(sysLoginLogQueryReqDTO);
        for (int i = 0; i < totalPage; i++) {
            sysLoginLogQueryReqDTO.setPageNumber(i + 1);
            List<SysLoginLogListRespDTO> sysLoginLogListRespDTOS = userLoginLogDubboService.queryLoginLogListByCondition(sysLoginLogQueryReqDTO);
            String sheetName = "第" + (i + 1) + "页";
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (SysLoginLogListRespDTO sysLoginLogListRespDTO : sysLoginLogListRespDTOS) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("loginTime", sysLoginLogListRespDTO.getLoginTime());
                dataMap.put("loginIp", sysLoginLogListRespDTO.getLoginIp());
                dataList.add(dataMap);
            }
            excelExport.wirteExcel(sheetName, dataList);
        }
        excelExport.writeEnd();
    }

}
