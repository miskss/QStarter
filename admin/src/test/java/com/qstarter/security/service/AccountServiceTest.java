package com.qstarter.security.service;

import com.qstarter.core.model.AccessToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;

@SpringBootTest
@ActiveProfiles("local")
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void getToken() throws URISyntaxException {

        OAuth2AccessToken jwtpass = accountService.getToken("admin.admin", "jwtpass");


        System.out.println(AccessToken.convert(jwtpass));
    }

}