package com.niiwoo.shield.manage.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台管理系统用户信息
 * Created by dell on 2017/9/2.
 */
@Getter
@Setter
public class UserDTO implements Serializable{
    private static final long serialVersionUID = 8491177998106012988L;

    private Long userId;     //用户ID
    private String userName;    //用户名称
    private String password;    //密码
    private String realName;    //真实姓名
    private String userCode;    //用户编号
    private String mobile;      //手机号码
    private String email;       //邮箱地址
    private String imageFile;  //用户头像
    private String salt;        //加密因子
    private Long departmentId;   //部门ID
    private  String departmentName;   //部门名称
    private Byte status;        //状态 0 已启用，1 已停用，2 已锁定
    private Date loginTime;     //最后登录时间
    private String loginIp;     //最后登录IP
    private Integer errorTimes; //密码错误次数
    private Date lockTime;       //用户名锁定截止时间
    private String createdUser;  //创建人
    private Date createdTime;    //创建时间
    private String updatedUser;  //修改人
    private Date updatedTime;    //修改时间

    private Long[] userIds;
    private Long[] roleIds;
    private String roleNames;     //角色名称，多个以英文逗号隔开
}
