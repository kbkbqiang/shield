package com.niiwoo.shield.manage.web.controller;



import com.niiwoo.shield.manage.web.vo.msg.request.AttachmentReqVO;
import com.niiwoo.shield.manage.web.vo.msg.response.AttachmentRespVO;
import com.niiwoo.tripod.consumer.component.FileUploadHandler;
import com.niiwoo.tripod.consumer.properties.FileUploadProperties;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attachment")
@Api(tags = "附件上传下载(吴越)", description = "/attachment")
@AuthIgnore
@Slf4j
public class AttachmentController {
    @Autowired
    private FileUploadHandler fileUploadHandler;

    @PostMapping("/uploadAttachment")
    @ResponseBody
    public Result<List<AttachmentRespVO>> uploadAttachment(HttpServletRequest request) {
        //上传附件
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");
        List<AttachmentRespVO> attachments = new ArrayList<>();
        //检验附件的扩展名和大小
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new BizException("BSC10002", "文件不能为空");
            } else {
                if (!fileUploadHandler.checkAttachmentUploadMaxSize(file.getSize())) {
                    throw new BizException("BSC10002", "文件大小超过上限");
                }
                String fileName = file.getOriginalFilename();
                String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!fileUploadHandler.checkAttachmentExtNames(extName)) {
                    throw new BizException("BSC10002", "文件类型不对");
                }

                try {
                    FileUploadProperties.FileUploadResult fileUploadResult = fileUploadHandler.saveAttachment(file.getBytes(), extName);
                    AttachmentRespVO attachmentVO = new AttachmentRespVO();
                    attachmentVO.setAttachmentName(fileName);
                    attachmentVO.setAttachmentPath(fileUploadResult.getImageUrl());
                    attachments.add(attachmentVO);
                } catch (IOException e) {
                    log.error("保存附件异常", e);
                    throw new BizException("US_300001");
                }
            }
        }
        return Result.with(attachments);
    }

    @PostMapping("downloadAttachment")
    public void downloadAttachment(HttpServletResponse response, AttachmentReqVO vo) {
        ServletOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            String attachmentPath = fileUploadHandler.getAttachmentPath(vo.getAttachmentPath());
            log.info("attachmentPath:", attachmentPath);
            File file = new File(attachmentPath);
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(vo.getAttachmentName().getBytes(), "ISO8859-1"));
            outputStream = response.getOutputStream();
            fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("下载附件异常", e);
            throw new BizException("US_300002");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                log.error("下载附件异常", e);
            }
        }
    }
}
