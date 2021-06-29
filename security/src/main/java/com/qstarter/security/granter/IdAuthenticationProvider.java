package com.qstarter.security.granter;

import com.qstarter.security.service.SystemUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 自定义的 登录验证器
 *
 * @author peter
 * date: 2019-09-24 10:53
 **/
public class IdAuthenticationProvider implements AuthenticationProvider {

    private final SystemUserDetailService service;


    public IdAuthenticationProvider(SystemUserDetailService service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long userId = (Long) authentication.getPrincipal();
        //拿到用户id，组装用户信息
        UserDetails userDetails = service.getUserDetails(userId);
        IdAuthenticationToken idAuthenticationToken = new IdAuthenticationToken(userDetails, userDetails.getAuthorities());
        idAuthenticationToken.setDetails(authentication.getDetails());
        return idAuthenticationToken;
    }


    /**
     * 判断是否支持的 认证 类型
     *
     * @param authentication 在过滤链上收到的 认证对象
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return Objects.equals(IdAuthenticationToken.class, authentication);
    }
}
