package com.niiwoo.shield.manage.web.util;

import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import org.apache.shiro.SecurityUtils;

/**
 * Created by dell on 2017/11/09.
 */
public class CommonUtil {

    //获取当前操作用户名
    public static String getCurrentUser(){
        UserAuthPrincipal userAuthPrincipal = (UserAuthPrincipal) SecurityUtils.getSubject().getPrincipal();
        return userAuthPrincipal.getPrincipal();
    }

    //获取当前用户真实姓名
    public static String getCurrentUserRealName(){
        return (String)SecurityUtils.getSubject().getSession(false).getAttribute("realName");
    }

    //获取当前操作用户ID
    public static Long getCurrentUserId(){
        UserAuthPrincipal userAuthPrincipal = (UserAuthPrincipal) SecurityUtils.getSubject().getPrincipal();
        return Long.valueOf(userAuthPrincipal.getUserId());
    }

}
