package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by luosiwen on 2017/8/26.
 */
@Setter
@Getter
public class WorkTaskUserInfoDTO implements Serializable{


    private static final long serialVersionUID = 8983035967100497393L;
    /**
     * 主键跟用户表ID保持一直
     */
    private Long userId;
    /**
     * 审核员姓名
     */
    private String realName;
    /**
     * 审核员编号
     */
    private String userCode;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 任务数量
     */
    private Integer taskNum;
}
