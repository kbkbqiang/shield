package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志实体类
 * Created by dell on 2017/12/9.
 * Description：civet-manage-parent
 */
@Setter
@Getter
public class SysLoginLogDTO implements Serializable{
    private static final long serialVersionUID = -2031414829151341958L;

    private Long id;         //主键ID
    private String userName;    //用户名称
    private Date loginTime;   //登录时间
    private String loginIp;    //登录IP
    private Integer status;     //登录状态 1 成功，2 失败
    private String msg;         //状态描述
}
