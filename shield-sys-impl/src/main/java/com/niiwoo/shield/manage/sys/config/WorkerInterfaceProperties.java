package com.niiwoo.shield.manage.sys.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by zhuhecheng on 2017/11/28.
 */
@ConfigurationProperties(WorkerInterfaceProperties.PREFIX)
@Setter
@Getter
public class WorkerInterfaceProperties {
    public static final String PREFIX = "worker-interface";

    //极速借审核员添加接口URL
    private String flAuditorAddUrl;
    //极速借审核员删除接口URL
    private String flAuditorDeleteUrl;
    //极速借跟踪员添加接口URL
    private String flFollowerAddUrl;
    //极速借跟踪员删除接口URL
    private String flFollowerDeleteUrl;
}
