package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Setter
@Getter
@ApiModel
public class UploadImageVO implements Serializable{
    private static final long serialVersionUID = -3042399112495309480L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户头像（base64字符串）")
    private String imageFile;
}
