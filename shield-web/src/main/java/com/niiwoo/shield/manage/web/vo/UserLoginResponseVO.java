package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登录响应
 * Created by dell on 2017/12/9.
 * Description：civet-manage-parent
 */
@Setter
@Getter
@ApiModel
@NoArgsConstructor
public class UserLoginResponseVO implements Serializable{

    private static final long serialVersionUID = -511178632944782633L;

    @ApiModelProperty(value = "接口响应码", required = true)
    private String respCode = "0000";

    @ApiModelProperty(value = "失败时提示信息")
    private String message = "success";

    @ApiModelProperty(value = "业务数据")
    private Object data;

    public UserLoginResponseVO(String message) {
        this.message = message;
    }

    public UserLoginResponseVO(String respCode, String message) {
        this.respCode = respCode;
        this.message = message;
    }

    public UserLoginResponseVO(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public UserLoginResponseVO(String respCode, String message, Object data) {
        this.respCode = respCode;
        this.message = message;
        this.data = data;
    }
}
