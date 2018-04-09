package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/21.
 * Description：shield-parent
 */
@Setter
@Getter
@ApiModel
public class QueryButtonsRequestVO implements Serializable{

    private static final long serialVersionUID = -9130709412160440204L;

    @ApiModelProperty("菜单ID")
    private Long menuId;
}
