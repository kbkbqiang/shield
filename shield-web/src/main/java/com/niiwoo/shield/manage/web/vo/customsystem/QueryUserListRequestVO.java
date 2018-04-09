package com.niiwoo.shield.manage.web.vo.customsystem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 客服系统查询用户列表参数
 * @author TomXue
 * @since 2018-02-06
 */
@Setter
@Getter
public class QueryUserListRequestVO implements Serializable{
    private static final long serialVersionUID = 4937590626713544176L;

    @ApiModelProperty("手机号码列表")
    @NotBlank(message = "手机号码列表不能为空")
    private List<String> mobileList;

    @ApiModelProperty("注册开始时间")
    @NotBlank(message = "注册开始时间不能为空")
    private String registerStartDate;

    @ApiModelProperty("注册结束时间")
    @NotNull(message = "注册结束时间不能为空")
    private String registerEndDate;
}
