package com.qstarter.core.thirdly_service.sms;

import org.springframework.stereotype.Component;

/**
 * @author peter
 * date: 2020-01-12 10:28
 **/
@Component
public class DefaultSmsSendService implements SmsSendSevice {
    @Override
    public boolean sendSmsCode(String code, String... phoneNumber) {
        return true;
    }

    @Override
    public Integer smsExpireTimeUnitSeconds() {
        return 5;
    }
}
