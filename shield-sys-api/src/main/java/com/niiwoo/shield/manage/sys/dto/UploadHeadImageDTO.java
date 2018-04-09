package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class UploadHeadImageDTO implements Serializable{

    private static final long serialVersionUID = 5318176022910360855L;
    private Long userId;     //用户ID
    private String name;     //用户名
    private String imageFile;  //用户头像
}
