package com.qstarter.core.thirdly_service.sms;

/**
 * 短信服务
 *
 * @author peter
 * date: 2019-09-24 14:01
 **/
public interface SmsService {

    /**
     * 生成短信验证码
     *
     * @return 短信码
     */
    String generateSmsCode();

    /**
     * 发送短信
     * 验证码通过{@link #generateSmsCode()} 获得
     *
     * @param phoneNumber 手机号
     * @return 是否发送成功
     */
    boolean sendSms(String phoneNumber);

    /**
     * 验证短信验证码是否正确
     *
     * @param phoneNumber 手机号
     * @param smsCode     验证码
     * @return 是否正确 {@code true} 匹配 or {@code false} 不匹配
     */
    boolean isMatch(String phoneNumber, String smsCode);
}
