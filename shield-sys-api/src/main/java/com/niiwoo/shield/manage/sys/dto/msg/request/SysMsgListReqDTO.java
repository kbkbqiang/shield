package com.niiwoo.shield.manage.sys.dto.msg.request;

import com.niiwoo.tripod.provider.dto.request.PageRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class SysMsgListReqDTO extends PageRequestDTO {
    private String msgTitle;

    private Date sendStartTime;

    private Date sendEndTime;
}
