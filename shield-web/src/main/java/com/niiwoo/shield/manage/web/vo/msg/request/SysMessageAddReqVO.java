package com.niiwoo.shield.manage.web.vo.msg.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@ApiModel
public class SysMessageAddReqVO {

    @ApiModelProperty(value = "通知标题")
    @NotBlank(message = "标题不能为空")
    private String msgTitle;

    @ApiModelProperty(value = "发送时间")
    @NotNull(message = "发送时间不能为空")
    private Date sendTime;

    @ApiModelProperty(value = "通知内容")
    @NotBlank(message = "通知内容不能为空")
    private String msgContent;

    @ApiModelProperty(value = "部门ID列表,如果发送对象为全部，只用传0过来")
    @NotNull(message = "部门ID列表不能为空")
    @Size(min = 1, message = "部门ID列表不能为空")
    private List<Long> departmentIdList;

    @ApiModelProperty(value = "附件ID列表")
    private List<AttachmentReqVO> attachmentList;

}
