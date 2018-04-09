package com.niiwoo.shield.manage.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/11.
 * Description：civet-manage-parent
 */
@Setter
@Getter
public class AuthCodeDTO implements Serializable{

    private static final long serialVersionUID = 5036392869301583860L;

    //手机号
    private String mobilePhone;

    //用户Id
    private Long userId;

    //验证码类型 1：短信验证码  2：图片验证码
    private Integer codeType;

    //验证码
    private String code;

    //图片验证码 base64 字符串
    private String imageCode;

    //是否忽略计数   true:忽略  false:不忽略
    private Boolean ignore;
}
