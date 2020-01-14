package com.qstarter.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 在{@link #timeToLive} 时间内 ，短信接口被调用的次数超过{@link #sendTimes} 次数后，
 * 则封禁该ip {@link #theBanTime} 时间，封禁期间不能再调用短信发送接口，防止恶意发送短信
 *
 * @author peter
 * date: 2019-10-29 09:46
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.sms")
@Data
public class SmsInterceptorConfig {

    /**
     * 短信发送的次数
     */
    private long sendTimes = 5;

    /**
     * ip地址调用记录存留的时长
     */
    private Duration timeToLive = Duration.ofMinutes(1);

    /**
     * ip 封禁的时长
     */
    private Duration theBanTime = Duration.ofHours(24);


    /**
     * 是否开启万能短信验证码
     */
    private boolean specialSmsCodeOpen = true;

    /**
     * 万能的短信验证码
     */
    private String specialSmsCode = "1234";


}

