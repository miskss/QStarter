package com.qstarter.security.config;

import com.qstarter.core.config.FileProperties;
import com.qstarter.core.model.GenericErrorMessage;
import com.qstarter.security.enums.RoleInSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author peter
 * date: 2019-09-04 17:43
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private ResourceServerTokenServices tokenServices;
    private FileProperties fileProperties;

    public ResourceServerConfig(ResourceServerTokenServices tokenServices, FileProperties fileProperties) {
        this.tokenServices = tokenServices;
        this.fileProperties = fileProperties;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices)
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String imgPath = fileProperties.getImgPath() + "/**";
        http
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/app/task","/api/app/attendance","/api/app/account/gold").permitAll()
                .antMatchers("/api/public/**", "/doc.html", "/webjars/**", "/swagger-resources", "/v2/api-docs", "/favicon.ico", imgPath).permitAll()
                .antMatchers("/api/web/**").hasAnyAuthority(RoleInSystem.WEB.name())
                .antMatchers("/api/app/**").hasAnyAuthority(RoleInSystem.APP.name())
//                .anyRequest()
//                .authenticated()
        ;
    }

    @Bean
    public WebResponseExceptionTranslator<GenericErrorMessage> exceptionTranslator() {
        return new SystemWebResponseExceptionTranslator();
    }


    /**
     * Inject your custom exception translator into the OAuth2 {@link AuthenticationEntryPoint}.
     *
     * @return AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        final OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setExceptionTranslator(exceptionTranslator());
        return entryPoint;
    }

    /**
     * Classic Spring Security stuff, defining how to handle {@link AccessDeniedException}s.
     * Inject your custom exception translator into the OAuth2AccessDeniedHandler.
     * (if you don't addOrUpdate this access denied exceptions may use a different format)
     *
     * @return AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        final OAuth2AccessDeniedHandler handler = new OAuth2AccessDeniedHandler();
        handler.setExceptionTranslator(exceptionTranslator());
        return handler;
    }


}
