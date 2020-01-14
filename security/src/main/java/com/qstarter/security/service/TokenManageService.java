package com.qstarter.security.service;

import com.qstarter.security.entity.SystemUser;
import com.qstarter.security.repository.SystemUserRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author peter
 * date: 2019-09-11 10:49
 **/
@Service
public class TokenManageService {

    private TokenStore tokenStore;
    private SystemUserRepository userRepository;

    public TokenManageService(TokenStore tokenStore, SystemUserRepository userRepository) {
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
    }

    public void removeToken(Long userId) {
        Collection<OAuth2AccessToken> tokens = findToken(userId);
        tokens.forEach(this::accept);
    }

    public void removeTokenAndRefreshToken(String systemUserName) {
        Collection<OAuth2AccessToken> tokens = findToken(systemUserName);
        tokens.forEach(this::accept1);
    }



    private void accept1(OAuth2AccessToken auth2AccessToken) {
        if (auth2AccessToken == null) return;
        tokenStore.readRefreshToken(auth2AccessToken.getValue());
        tokenStore.removeAccessToken(auth2AccessToken);
    }


    private Collection<OAuth2AccessToken> findToken(Long userId) {
        SystemUser systemUser = userRepository.findOne(userId);
        return tokenStore.findTokensByClientIdAndUserName(AccountService.CLIENT, systemUser.getUsername());
    }

    private Collection<OAuth2AccessToken> findToken(String systemUserName) {
        return tokenStore.findTokensByClientIdAndUserName(AccountService.CLIENT, systemUserName);
    }

    private void accept(OAuth2AccessToken token) {
        tokenStore.removeAccessToken(token);
    }
}
