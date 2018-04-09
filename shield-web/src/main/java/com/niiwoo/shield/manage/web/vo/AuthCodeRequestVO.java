package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 验证码
 * Created by dell on 2017/8/16.
 */
@Setter
@Getter
public class AuthCodeRequestVO implements Serializable {

    private static final long serialVersionUID = 1416139605993750437L;
//    @ApiModelProperty("手机号")
//    @NotBlank(message = "手机号不能为空")
//    private String mobilePhone;
//
//    @ApiModelProperty("用户ID")
//    @NotNull(message = "用户ID不能为空")
//    private Long userId;

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty("验证码类型 1：短信验证码  2：图片验证码")
    private Integer codeType;

}
