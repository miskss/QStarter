package com.qstarter.security.config;

import com.qstarter.security.granter.IdAuthenticationProvider;
import com.qstarter.security.granter.IdTokenGranter;
import com.qstarter.security.service.SystemUserDetailService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author peter
 * date: 2019-09-03 16:57
 **/
@Configuration
@EnableAuthorizationServer
@ComponentScan(basePackages = "com.*")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter accessTokenConverter;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;
    private final SystemUserDetailService service;

    public AuthorizationServerConfig(TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, DataSource dataSource, SystemUserDetailService service) {
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
        this.service = service;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(dataSource);
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        ProviderManager providerManager = (ProviderManager) this.authenticationManager;

        //将自定义验证的 provider 添加进来
        providerManager.getProviders().add(new IdAuthenticationProvider(service));

        endpoints.tokenStore(tokenStore)
                .reuseRefreshTokens(false)
                .accessTokenConverter(accessTokenConverter)
                .authenticationManager(providerManager)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter(endpoints))
        ;


    }


    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {

        TokenGranter tokenGranter = endpoints.getTokenGranter();

        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(tokenGranter));

        /*
         * 将自定义的 granter 授权方式 添加到系统中去
         */
        IdTokenGranter idTokenGranter = new IdTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), this.authenticationManager);

        granters.add(idTokenGranter);

        return new CompositeTokenGranter(granters);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }
}
