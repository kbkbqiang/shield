package com.niiwoo.shield.manage.sys.service.local;


import com.niiwoo.shield.manage.base.constants.RedisConstants;
import com.niiwoo.shield.manage.base.enums.PlatformEnum;
import com.niiwoo.shield.manage.sys.dao.entity.SysLoginLog;
import com.niiwoo.shield.manage.sys.dao.entity.SysSmsRecord;
import com.niiwoo.shield.manage.sys.dao.entity.SysUser;
import com.niiwoo.shield.manage.sys.dao.mapper.SysLoginLogMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysSmsRecordMapper;
import com.niiwoo.shield.manage.sys.dao.mapper.SysUserMapperExt;
import com.niiwoo.shield.manage.sys.dto.AuthCodeDTO;
import com.niiwoo.shield.manage.sys.dto.SysLoginLogDTO;
import com.niiwoo.shield.manage.sys.enums.SmsRecordTypeEnum;
import com.niiwoo.shield.manage.sys.enums.UserStatusEnum;
import com.niiwoo.shield.manage.sys.enums.ValidateCodeEnum;
import com.niiwoo.shield.manage.sys.util.SysStringUtils;
import com.niiwoo.shield.manage.sys.util.VerifyCodeUtil;
import com.niiwoo.tripod.consumer.helper.Exceptions;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import com.niiwoo.tripod.sms.ISmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2017/12/22.
 * Description：shield-parent
 */
@Slf4j
@Service
public class UserLoginLocalService {

    @Value("${spring.profiles.active}")
    private String profile;     // 短信验证码开关
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ISmsSendService smsSendService;
    @Autowired
    private SysUserMapperExt sysUserMapperExt;
    @Autowired
    private SysLoginLogMapperExt sysLoginLogMapperExt;
    @Autowired
    private SysSmsRecordMapper sysSmsRecordMapper;

    /**
     * 记录登录失败相关数据至用户表
     *
     * @param userName
     */
    @Transactional
    public void saveFailLoginData(String userName) {
        String redisKey = RedisConstants.SD_MANAGER_LOGIN_RETRY_TIMES_PREFIX + userName;
        Long retryTimes = redisTemplate.opsForValue().increment(redisKey, 1);

        //更新用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        sysUser.setErrorTimes(retryTimes.intValue());
        sysUser.setUpdatedUser(userName);
        Date currentTime = new Date();
        sysUser.setUpdatedTime(currentTime);

        //锁定账号
        if (retryTimes >= 5) {
            sysUser.setStatus(UserStatusEnum.LOCKED.getStatusCode());
            sysUser.setLockTime(currentTime);
        }
        updateLoginData(sysUser);
    }

    /**
     * 更新用户最后登录IP和时间
     *
     * @param sysUser
     */
    @Transactional
    public void updateLoginData(SysUser sysUser) {
        sysUserMapperExt.updateUserLoginData(sysUser);
    }


    /**
     * 添加登录日志
     *
     * @param sysLoginLogDTO
     */
    @Transactional
    public void insertSysLoginLog(SysLoginLogDTO sysLoginLogDTO) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setId(snowflakeIdWorker.nextId());
        sysLoginLog.setUserName(sysLoginLogDTO.getUserName());
        sysLoginLog.setLoginIp(sysLoginLogDTO.getLoginIp());
        sysLoginLog.setLoginTime(sysLoginLogDTO.getLoginTime());
        sysLoginLog.setMsg(sysLoginLogDTO.getMsg());
        sysLoginLog.setStatus(sysLoginLogDTO.getStatus().byteValue());
        sysLoginLogMapperExt.insertSelective(sysLoginLog);
    }


    /**
     * 记录登录成功相关数据至用户表
     *
     * @param userName
     * @param loginIp
     */
    @Transactional
    public void saveSuccessLoginData(String userName, String loginIp) {
        String redisKey = RedisConstants.SD_MANAGER_LOGIN_RETRY_TIMES_PREFIX + userName;
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }

        //更新用户信息
        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setLoginIp(loginIp);
        user.setLoginTime(new Date());
        user.setErrorTimes(0);
        user.setUpdatedUser(userName);
        Date currentTime = new Date();
        user.setUpdatedTime(currentTime);

        updateLoginData(user);
    }


    /**
     * 神盾发送短信验证码
     *
     * @param authCodeDTO
     * @param limitCount
     * @param templateValue
     * @return
     */
    public AuthCodeDTO mgrSendSmsCode(AuthCodeDTO authCodeDTO, Integer limitCount, String templateValue) {
        String mobilePhone = authCodeDTO.getMobilePhone();
        Boolean ignore = authCodeDTO.getIgnore();
        log.info("【SysUserLoginLocalService -> mgrSendSmsCode】入参手机号：{}", mobilePhone);

        try {
            String countKey = RedisConstants.SMS_CODE_COUNT_PREFIX
                    + mobilePhone + "-"
                    + simpleDateFormat.format(new Date()) + "-"
                    + PlatformEnum.PLATFORM_SD_MANAGE.getPlatformId();      // 短信计数key

            BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(countKey);
            boundValueOperations.expire(RedisConstants.COUNT_EXPIRE_TIME, TimeUnit.SECONDS);    //24小时过期

            String smsCountStr = (String) boundValueOperations.get();
            Integer smsCount = StringUtils.isNotBlank(smsCountStr) ? Integer.valueOf(smsCountStr) : 0;

            //获取图片验证码
            if (smsCount >= limitCount && !ignore) {
                authCodeDTO.setCodeType(ValidateCodeEnum.IMAGECODE.getType());
                authCodeDTO.setImageCode(flushImageCode(mobilePhone));

                //发送短信验证码
            } else {
                authCodeDTO.setCodeType(ValidateCodeEnum.SMSCODE.getType());

                String code = "888888";
                //当使用的不是测试环境，则采用发短信的方式
                if (profile != null) {
                    // 生成一个6位数的数字验证码
                    code = SysStringUtils.getSmsCode(6, "0123456789".toCharArray());

                    long startTime = System.currentTimeMillis();
                    log.info("发送短信验证码开始时间：" + startTime);
                    //发送短信验证码
                    CompletableFuture<Boolean> result = smsSendService.sendNormal(mobilePhone, templateValue, null, code);
                    if (!result.get()) {
                        log.error("【SysUserLoginLocalService -> mgrSendSmsCode】发送短信验证码失败！  用户手机号:{}", mobilePhone);
                        Exceptions.throwBizException("MS_200001");
                    } else {
                        // 存短信发送记录
                        SysSmsRecord record = new SysSmsRecord();
                        record.setId(snowflakeIdWorker.nextId());
                        record.setUserId(authCodeDTO.getUserId());
                        record.setMobilePhone(mobilePhone);
                        record.setType(SmsRecordTypeEnum.LOGIN_SMS_CODE.getCode());
                        record.setSmsContent(code);
                        Date date = new Date();
                        record.setCreateTime(date);
                        record.setUpdateTime(date);
                        sysSmsRecordMapper.insertSelective(record);
                    }
                    long endTime = System.currentTimeMillis();
                    log.info("发送短信验证码结束时间：" + endTime + ", 耗时：" + (endTime - startTime) + "ms");
                }

                // 将生成的验证码存入到缓存中
                String key = RedisConstants.VERIFY_CODE_PREFIX + mobilePhone + "_" + PlatformEnum.PLATFORM_SD_MANAGE.getPlatformId();
                redisTemplate.opsForValue().set(key, code, RedisConstants.CODE_EXPIRE_TIME, TimeUnit.SECONDS);
                authCodeDTO.setCode(code);
                //短信计数加 1
                boundValueOperations.increment(1);
                log.info("【当前用户:{},短信计数key:{},次数count:{}】", mobilePhone, countKey, boundValueOperations.get());
            }
        } catch (Exception e) {
            log.error("神盾发送短信验证码mgrSendSmsCode 异常！", e);
            return null;
        }
        return authCodeDTO;
    }


    /**
     * 刷新图片验证码
     *
     * @param mobileNo
     * @return
     */
    public String flushImageCode(String mobileNo) {
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        // 将生成的验证码存入到缓存中
        String key = RedisConstants.VERIFY_CODE_PREFIX + mobileNo + "_" + PlatformEnum.PLATFORM_SD_MANAGE.getPlatformId();
        redisTemplate.opsForValue().set(key, verifyCode, RedisConstants.CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        return VerifyCodeUtil.getBase64String(verifyCode);
    }


}
