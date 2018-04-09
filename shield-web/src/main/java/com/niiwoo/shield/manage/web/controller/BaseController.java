package com.niiwoo.shield.manage.web.controller;

import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@RestController
public class BaseController {

    /**
     * 获取当前用户认证信息
     * @return
     */
    public static UserAuthPrincipal getCurrentUser(){
        return (UserAuthPrincipal) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前用户id
     * @return
     */
    public static Long getCurrentUserId(){
        UserAuthPrincipal principal = (UserAuthPrincipal) SecurityUtils.getSubject().getPrincipal();
        return Long.valueOf(principal.getUserId());
    }

    //获取当前用户真实姓名
    public static String getCurrentUserRealName(){
        return (String)SecurityUtils.getSubject().getSession(false).getAttribute("realName");
    }

    /**
     * 用户密码加密
     * @param password
     * @param salt
     * @return
     */
    public static String getMd5HashPwd(String password,String salt){
        return new Md5Hash(password, salt, 3).toString(); //迭代次数为3
    }

}