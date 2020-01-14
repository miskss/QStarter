package com.qstarter.security.service;

import com.qstarter.core.model.AccessToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

@SpringBootTest
@RunWith(SpringRunner.class)
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