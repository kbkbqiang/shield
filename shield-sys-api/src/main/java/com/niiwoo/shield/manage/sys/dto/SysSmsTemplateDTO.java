package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dell on 2017/12/11.
 * Description：civet-parent
 */
@Setter
@Getter
public class SysSmsTemplateDTO implements Serializable{
    private static final long serialVersionUID = 890331875542996062L;

    private Integer id;
    private String templateCode;        //模板编码
    private String smsType;             //通知类型/模式1:通知,2:短信,3:通知+短信
    private String templateTitle;       //模板标题
    private String templateValue;       //模板内容
    private String description;         //描述
    private Date createTime;            //创建时间
    private Date updateTime;            //更改时间
    private String delFlag;             //状态：N-正常;D-删除
    private Long updateManager;         //修改人Id
    private Long createManager;         //创建人Id
}
