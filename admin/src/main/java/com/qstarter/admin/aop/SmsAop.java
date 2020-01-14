package com.qstarter.admin.aop;

import com.qstarter.core.config.SmsInterceptorConfig;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.constant.RedisKey;
import com.qstarter.core.utils.RemoteIpHelper;
import com.qstarter.core.utils.redis.RedisUtils;
import com.qstarter.security.utils.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 拦截短信发送接口
 *
 * @author peter
 * date: 2019-10-29 10:37
 **/
@Aspect
@Component
@Slf4j
public class SmsAop {

    private SmsInterceptorConfig config;

    public SmsAop(SmsInterceptorConfig smsInterceptorConfig) {
        this.config = smsInterceptorConfig;
    }


    @Pointcut("execution(public com.qstarter.core.model.GenericMsg com.qstarter.admin.controller.LoginController.sendSms(String))"
            +"|| execution(public com.qstarter.core.model.GenericMsg com.qstarter.app.controller.AppUserLoginController.sendSms(String))"
    )
    public void pointHandler() {
    }


    @Around(value = "pointHandler()")
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {

        String requestIp = RemoteIpHelper.getClientIpAddress(ContextHolder.getRequest());
        long banTime = config.getTheBanTime().getSeconds();
        long liveTime = config.getTimeToLive().getSeconds();

        //先查看ip有没有被封禁
        String banKey = RedisKey.BAN_IP + requestIp;
        if (RedisUtils.hasKey(banKey)) {
            Long keyExpireTime = RedisUtils.getKeyExpireTime(banKey, TimeUnit.MINUTES);
            throw new SystemServiceException(ErrorMessageEnum.SMS_IP_BAN, String.format(ErrorMessageEnum.SMS_IP_BAN.getMsg(), keyExpireTime));
        }

        Object proceed = pjp.proceed();

        String requestKey = RedisKey.CODE_IP + requestIp;

        if (RedisUtils.hasKey(requestKey)) {
            Long num = RedisUtils.incrString(requestKey);
            long sendTimes = config.getSendTimes();
            if (Objects.equals(num, sendTimes)) {
                log.info("ip【{}】发送短信频率异常，在【{}】秒内调用了【{}】次短信接口，封禁该ip，【{}】分钟后解封", requestIp, liveTime, sendTimes, config.getTheBanTime().toMinutes());
                RedisUtils.setStringValueExpire(banKey, requestIp, banTime);
            }
        } else {
            RedisUtils.setStringValueExpire(requestKey, 1, liveTime);
        }
        return proceed;
    }

}
