package com.qstarter.core.thirdly_service.sms;

import com.google.common.base.Strings;
import com.qstarter.core.config.SmsInterceptorConfig;
import com.qstarter.core.constant.RedisKey;
import com.qstarter.core.utils.redis.RedisUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author peter
 * date: 2019-12-04 16:50
 **/
@Component
public class SmsServiceImpl implements SmsService {

    private SmsInterceptorConfig config;
    private SmsSendSevice sendSmsService;

    public SmsServiceImpl(SmsInterceptorConfig config, SmsSendSevice sendSmsService) {
        this.config = config;
        this.sendSmsService = sendSmsService;
    }

    @Override
    public String generateSmsCode() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    @Override
    public boolean sendSms(String phoneNumber) {
        String redisKey = getRedisKey(phoneNumber);

        Long keyExpireTime = RedisUtils.getKeyExpireTime(redisKey, TimeUnit.SECONDS);

        //如果剩余时间大于30s 则不在发送
        if (keyExpireTime != null && keyExpireTime > 30) {
            return true;
        }

        String code = generateSmsCode();
        boolean b = sendSmsService.sendSmsCode(code, phoneNumber);
        if (b) {
            RedisUtils.setStringValueExpire(redisKey, code, sendSmsService.smsExpireTimeUnitSeconds() * 60 + 10);
        }
        return b;
    }

    @Override
    public boolean isMatch(String phoneNumber, String smsCode) {
        if (config.isSpecialSmsCodeOpen()){
            if (Objects.equals(smsCode, config.getSpecialSmsCode())) return true;
        }
        String redisKey = getRedisKey(phoneNumber);
        String redisSmsCode = (String) RedisUtils.getStringValue(redisKey);
        if (Strings.isNullOrEmpty(redisSmsCode)) return false;
        if (Objects.equals(smsCode, redisSmsCode)) {
            RedisUtils.delete(redisKey);
            return true;
        }
        return false;
    }

    private String getRedisKey(String phoneNumber) {
        return RedisKey.SMS_CODE_KEY + phoneNumber;
    }
}
