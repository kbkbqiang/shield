package com.niiwoo.shield.manage.sys.dto.msg.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
public class SysMsgDeleteReqDTO implements Serializable{
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "后台通知ID列表不能为空")
    @Size(min = 1, message = "后台通知ID列表不能为空")
    private List<Long> msgIdList;
}
