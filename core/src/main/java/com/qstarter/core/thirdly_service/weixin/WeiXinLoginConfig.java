package com.qstarter.core.thirdly_service.weixin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author peter
 * create: 2019-12-16 14:11
 **/
@Configuration
@ConfigurationProperties(prefix = "third.service.wx.login")
@Data
public class WeiXinLoginConfig {

    private String appId;

    private String secret;
}
