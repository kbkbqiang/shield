package com.niiwoo.shield.manage.sys.enums;

import lombok.Getter;

/**
 * Created by dell on 2017/8/10.
 */
@Getter
public enum WorkTypeEnum {
    FL_AUDIT_TASK(1,"极速借审核员"),
    FL_FOLLOW_TASK(2,"极速借跟踪员");

    private final Integer workType;
    private final String roleName;

    WorkTypeEnum(Integer workType, String roleName){
        this.workType = workType;
        this.roleName = roleName;
    }
}
