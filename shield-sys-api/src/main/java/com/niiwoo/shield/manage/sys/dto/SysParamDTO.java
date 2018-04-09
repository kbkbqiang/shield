package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SysParamDTO implements Serializable{
    private static final long serialVersionUID = -4018687208746330340L;
    private Integer id;

    private String paramType;

    private String paramKey;

    private String paramValue;

    private Date createTime;

    private Date updateTime;

    private String delFlag;

    private Long updateManager;

    private Long createManager;

    private String description;
}
