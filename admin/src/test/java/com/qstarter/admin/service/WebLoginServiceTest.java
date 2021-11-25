package com.qstarter.admin.service;

import com.qstarter.security.service.AccountService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest(classes = {WebLoginService.class, AccountService.class})
public class WebLoginServiceTest {

    @Autowired
    private WebLoginService andRegisterService;

    @Test
    public void login() throws URISyntaxException {
        andRegisterService.login("admin.admin","jwtpass");
    }
}