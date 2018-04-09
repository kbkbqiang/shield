package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.niiwoo.shield.manage.base.constants.RedisConstants;
import com.niiwoo.shield.manage.base.enums.PlatformEnum;
import com.niiwoo.shield.manage.base.utils.StringUtil;
import com.niiwoo.shield.manage.sys.dto.AuthCodeDTO;
import com.niiwoo.shield.manage.sys.dto.SysLoginLogDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.shield.manage.sys.service.UserLoginDubboService;
import com.niiwoo.shield.manage.web.enums.LoginTypeEnum;
import com.niiwoo.shield.manage.web.enums.RespMsgEnum;
import com.niiwoo.shield.manage.web.util.ConvertUtils;
import com.niiwoo.shield.manage.web.vo.AuthCodeRequestVO;
import com.niiwoo.shield.manage.web.vo.UserLoginResponseVO;
import com.niiwoo.shield.manage.web.vo.UserLoginVO;
import com.niiwoo.shield.manage.web.vo.login.response.SysLoginTypeRespVO;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.enums.DeviceTypeEnum;
import com.niiwoo.tripod.web.shiro.UserAuthPrincipal;
import com.niiwoo.tripod.web.shiro.UsernamePasswordDeviceToken;
import com.niiwoo.tripod.web.util.RequestUtil;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/loginManege")
@Api(tags = "登录管理", description = "/loginManege")
public class LoginManageController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Reference(version = "1.0.0")
    private UserLoginDubboService userLoginDubboService;
    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;

    /**
     * 登录
     *
     * @param request
     * @param loginVO
     * @return
     */
    @PostMapping("/login")
    @AuthIgnore
    public UserLoginResponseVO login(HttpServletRequest request, @RequestBody @Validated UserLoginVO loginVO) {
        UserLoginResponseVO loginResponseVO = new UserLoginResponseVO();
        //参数校验
        if (loginVO.getHaveSmsCode() && StringUtils.isBlank(loginVO.getSmsCode())) {
            return new UserLoginResponseVO(RespMsgEnum.NO_SUPPORT_EMPTY.getCode(), "验证码不能为空");
        }
        String requestIp = RequestUtil.getUserRealIP(request);
        loginVO.setLoginIp(requestIp);

        boolean successFlag = false;
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordDeviceToken usernamePasswordDeviceToken = new UsernamePasswordDeviceToken(loginVO.getUserName(), loginVO.getPassword(), DeviceTypeEnum.H5);
            subject.login(usernamePasswordDeviceToken);
            UserAuthPrincipal userAuthPrincipal = (UserAuthPrincipal) subject.getPrincipal();

            successFlag = true;
            //用户名和密码都正确，如果是外网登录并且没有验证码，则返回要输入验证码
            if (!loginVO.getHaveSmsCode() && !isInnerIP(loginVO.getLoginIp())) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("needSmsCode", true);
                data.put("userId", userAuthPrincipal.getUserId());
                // 业务保留域，传递手机号，发送短信验证码需要手机号
                data.put("mobilePhone", userAuthPrincipal.getReserve());
                return new UserLoginResponseVO("需要验证码", data);

            } else if (loginVO.getHaveSmsCode()) {
                //校验验证码
                //从缓存中取出验证码
                String key = RedisConstants.VERIFY_CODE_PREFIX + userAuthPrincipal.getReserve() + "_" + PlatformEnum.PLATFORM_SD_MANAGE.getPlatformId();
                String serverCode = (String) redisTemplate.opsForValue().get(key);
                if (serverCode == null || !serverCode.equalsIgnoreCase(loginVO.getSmsCode().trim())) {
                    loginResponseVO = new UserLoginResponseVO(RespMsgEnum.IS_WRONG.getCode(), "验证码错误");
                    successFlag = false;

                    //记录登录失败相关数据至用户表
                    userLoginDubboService.saveFailLoginData(loginVO.getUserName());
                }
            }
        } catch (UnknownAccountException e) {
            loginResponseVO = new UserLoginResponseVO(RespMsgEnum.NO_EXISTS.getCode(), "该用户名不存在");
        } catch (LockedAccountException e) {
            loginResponseVO = new UserLoginResponseVO(RespMsgEnum.ACCOUNT_IS_LOCKED.getCode(), "请联系管理员解锁账号");
        } catch (DisabledAccountException e) {
            loginResponseVO = new UserLoginResponseVO(RespMsgEnum.ACCOUNT_STOP_USED.getCode(), "当前用户已被停用");
        } catch (AuthenticationException e) {
            loginResponseVO = new UserLoginResponseVO(RespMsgEnum.IS_WRONG.getCode(), "用户名或密码错误");

            //记录登录失败相关数据至用户表
            userLoginDubboService.saveFailLoginData(loginVO.getUserName());
        }

        if (successFlag) {
            // 记录登录成功相关数据至用户表
            userLoginDubboService.saveSuccessLoginData(loginVO.getUserName(), loginVO.getLoginIp());

            String sessionId = (String) subject.getSession().getId();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("token", sessionId);
            UserDTO user = userDubboService.queryUserByUserName(loginVO.getUserName());
            //清空密码相关数据再返回
            user.setPassword("");
            user.setSalt("");
            dataMap.put("user", user);
            SecurityUtils.getSubject().getSession(false).setAttribute("realName", user.getRealName());

            loginResponseVO.setData(dataMap);
            loginResponseVO.setMessage("登录成功");
        }

        //记录登录日志
        insertSysLoginLog(loginVO, successFlag, loginResponseVO.getMessage());

        return loginResponseVO;
    }


    /**
     * 记录登录日志
     *
     * @param loginVO
     * @param successFlag 是否登录成功
     * @param message     登录失败信息
     */
    public void insertSysLoginLog(UserLoginVO loginVO, boolean successFlag, String message) {
        //日志对象
        SysLoginLogDTO loginLog = new SysLoginLogDTO();
        loginLog.setUserName(loginVO.getUserName());
        loginLog.setLoginIp(loginVO.getLoginIp());
        loginLog.setLoginTime(new Date());
        loginLog.setMsg(message);

        if (successFlag) {
            loginLog.setStatus(1);
        } else {
            loginLog.setStatus(2);
        }
        userLoginDubboService.insertSysLoginLog(loginLog);
    }


    private boolean isInnerIP(String ipStr) {
        if (null == ipStr) {
            return true;
        }
//        String[] ipArr = ipStr.split("\\.");
//        long ipNumber = 0;
//        for (int i = 0; i < ipArr.length; i++) {
//            ipNumber += Integer.parseInt(ipArr[i]) << (3 - i) * 8;
//        }
//        if ((ipNumber >= 0x0A000000 && ipNumber <= 0x0AFFFFFF) || (ipNumber >= 0xAC100000 && ipNumber <= 0xAC1FFFFF) ||
//                (ipNumber >= 0xC0A80000 && ipNumber <= 0xC0A8FFFF) || (ipNumber >= 0x7F000000 && ipNumber <= 0x7FFFFFFF)) {
//            return true;
//        }
        if (ipStr.equals("61.144.217.106") || ipStr.equals("120.234.2.106") || ipStr.equals("218.17.157.50")) {
            return true;
        }
        return false;
    }


    /**
     * 发送验证码
     *
     * @return
     */
    @PostMapping("/sendSmsCode")
    @AuthIgnore
    public UserLoginResponseVO sendSmsCode(@RequestBody @Validated AuthCodeRequestVO requestVO) {
        UserDTO userDTO = userDubboService.queryUserByUserName(requestVO.getUserName());
        if (userDTO == null) {
            return new UserLoginResponseVO(RespMsgEnum.NO_EXISTS.getCode(), "该用户名不存在");
        }

        // 发送短信验证码
        AuthCodeDTO authCodeDTO = new AuthCodeDTO();
        authCodeDTO.setIgnore(true);
        authCodeDTO.setMobilePhone(userDTO.getMobile());
        authCodeDTO.setUserId(userDTO.getUserId());
        authCodeDTO.setCodeType(requestVO.getCodeType());
        AuthCodeDTO responseDto = userLoginDubboService.mgrSendSmsCode(authCodeDTO);

        if (responseDto != null) {
            return new UserLoginResponseVO("发送验证码成功");
        } else {
            return new UserLoginResponseVO(RespMsgEnum.SEND_SMS_CODE_FAIL.getCode(), "发送验证码失败");
        }

    }

    /**
     * 判断是否需要验证码登录
     *
     * @return
     */
    @PostMapping("/getLoginType")
    @AuthIgnore
    public Result<SysLoginTypeRespVO> getLoginType(HttpServletRequest request) {
        String requestIp = RequestUtil.getUserRealIP(request);
        SysLoginTypeRespVO sysLoginTypeRespVO = new SysLoginTypeRespVO();
        if (!isInnerIP(requestIp)) {
            sysLoginTypeRespVO.setLoginType(LoginTypeEnum.NEED_SMS_CODE.getValue());
        } else {
            sysLoginTypeRespVO.setLoginType(LoginTypeEnum.NOT_NEED_SMS_CODE.getValue());
        }
        return Result.with(sysLoginTypeRespVO);
    }


    /**
     * 注销
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.with("注销成功");
    }
}
