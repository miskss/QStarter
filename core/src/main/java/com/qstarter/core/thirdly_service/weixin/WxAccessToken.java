package com.qstarter.core.thirdly_service.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author peter
 * date: 2019-12-18 13:35
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class WxAccessToken extends ErrorMsg{

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
}
