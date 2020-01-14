package com.qstarter.core.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;


/**
 * 返回给用户的登录token信息
 *
 * @author peter
 * date: 2019-09-05 10:52
 **/
@Data
@ApiModel
public class AccessToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;


    public static AccessToken convert(OAuth2AccessToken auth2AccessToken) {

        AccessToken token = new AccessToken();

        token.setAccess_token(auth2AccessToken.getValue());
        token.setToken_type(OAuth2AccessToken.BEARER_TYPE);
        token.setExpires_in(auth2AccessToken.getExpiresIn());
        token.setRefresh_token(auth2AccessToken.getRefreshToken().getValue());

        return token;
    }
}
