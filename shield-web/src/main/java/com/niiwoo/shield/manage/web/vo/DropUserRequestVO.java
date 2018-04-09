package com.niiwoo.shield.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by dell on 2017/12/21.
 * Description：shield-parent
 */
@Setter
@Getter
@ApiModel
public class DropUserRequestVO implements Serializable{

    @ApiModelProperty("用户id列表")
    private Long[] userIds;
}
