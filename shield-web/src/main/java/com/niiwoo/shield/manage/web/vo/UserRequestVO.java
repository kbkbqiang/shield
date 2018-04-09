package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/21.
 * Description：shield-parent
 */
@ApiModel
@Setter
@Getter
public class UserRequestVO implements Serializable {

    private static final long serialVersionUID = 6257560545898007015L;

    @NotEmpty(message = "用户id不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
}
