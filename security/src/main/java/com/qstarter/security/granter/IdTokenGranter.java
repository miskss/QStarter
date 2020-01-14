package com.qstarter.security.granter;

import com.qstarter.security.entity.SystemUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 参照：{@link org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter}
 * <p>
 * 其他的登录方式：短信、三方登录
 * 由于整个系统中有 后台管理人员和 app 的 用户，且所有的用户登录信息全部整合到了{@link SystemUser} 中。
 * 所以除了username、password 登录外，其他的登录方式全部使用IdTokenGranter 来 登录，这些登录之前的验证逻辑全部在客户端，
 * 客户端 验证通过后 ，通过id 来 使用 IdTokenGranter 来进行系统的登录。
 * 网页后台的用户{@code WebUser}
 * app 用户 {@code AppUser }
 *
 * @author peter
 * date: 2019-09-24 10:33
 **/
public class IdTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "id";

    private final AuthenticationManager authenticationManager;

    public IdTokenGranter(AuthorizationServerTokenServices tokenServices,
                          ClientDetailsService clientDetailsService,
                          OAuth2RequestFactory requestFactory,
                          AuthenticationManager authenticationManager) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE, authenticationManager);
    }

    private IdTokenGranter(AuthorizationServerTokenServices tokenServices,
                           ClientDetailsService clientDetailsService,
                           OAuth2RequestFactory requestFactory,
                           String grantType,
                           AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String username = parameters.get("user_id");
        Long userId = Long.parseLong(username);
        Authentication userAuth = new IdAuthenticationToken(userId);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
