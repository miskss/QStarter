package com.qstarter.core.thirdly_service.sms;

/**
 * @author peter
 * date: 2020-01-12 10:13
 **/
public interface SmsSendSevice {


    /**
     * 发送短信验证码
     *
     * @param code        验证码
     * @param phoneNumber 手机号
     * @return 短信是否发送成功
     */
    boolean sendSmsCode(String code, String... phoneNumber);


    /**
     * 设置短信的有效时间
     *
     * @return Integer
     */
    Integer smsExpireTimeUnitSeconds();

}
