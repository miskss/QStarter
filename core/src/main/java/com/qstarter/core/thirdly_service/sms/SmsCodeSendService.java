package com.qstarter.core.thirdly_service.sms;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import org.springframework.stereotype.Service;

/**
 * 短信发送服务
 *
 * @author peter
 * date: 2019-10-28 15:57
 **/

@Service
public class SmsCodeSendService {

    private SmsService smsService;

    public SmsCodeSendService(SmsService smsService) {
        this.smsService = smsService;
    }

    /**
     * 发送短信
     *
     * @param phoneNumber 手机号
     */
    public void sendSmsCode(String phoneNumber) {
        boolean b = smsService.sendSms(phoneNumber);
        if (!b) {
            throw new SystemServiceException(ErrorMessageEnum.SMS_SEND_FAIL);
        }
    }

    /**
     * 验证 验证码
     *
     * @param phoneNumber 手机号
     * @param smsCode     短信码
     */
    public void isMatch(String phoneNumber, String smsCode) {
        //验证短信
        boolean match = smsService.isMatch(phoneNumber, smsCode);

        if (!match) {
            throw new SystemServiceException(ErrorMessageEnum.SMS_CODE_IS_NOT_MATCH);
        }
    }

}
