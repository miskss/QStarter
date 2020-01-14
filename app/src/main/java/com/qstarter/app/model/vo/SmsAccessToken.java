package com.qstarter.app.model.vo;

import com.qstarter.core.model.AccessToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author peter
 * date: 2019-11-11 16:10
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SmsAccessToken extends AccessToken {

    @ApiModelProperty("短信注册登录后用户是否已设置密码，如果已设置则为true,没有则为false需要弹出设置密码的界面让用户设置密码")
    private boolean hasPassword;

    public static SmsAccessToken convert(OAuth2AccessToken auth2AccessToken, boolean hasPassword) {

        SmsAccessToken token = new SmsAccessToken();

        token.setAccess_token(auth2AccessToken.getValue());
        token.setToken_type(OAuth2AccessToken.BEARER_TYPE);
        token.setExpires_in(auth2AccessToken.getExpiresIn());
        token.setRefresh_token(auth2AccessToken.getRefreshToken().getValue());
        token.setHasPassword(hasPassword);

        return token;
    }

}
