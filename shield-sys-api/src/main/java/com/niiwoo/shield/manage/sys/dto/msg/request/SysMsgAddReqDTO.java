package com.niiwoo.shield.manage.sys.dto.msg.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
public class SysMsgAddReqDTO implements Serializable {
    @NotBlank(message = "标题不能为空")
    private String msgTitle;

    @NotNull(message = "发送时间不能为空")
    private Date sendTime;

    @NotBlank(message = "通知内容不能为空")
    private String msgContent;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "部门ID列表不能为空")
    @Size(min = 1, message = "部门ID列表不能为空")
    private List<Long> departmentIdList;

    private List<AttachmentReqDTO> attachmentReqDTOList;

}
