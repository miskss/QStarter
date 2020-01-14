package com.qstarter.admin.service;

import com.qstarter.security.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebLoginService.class, AccountService.class})
public class WebLoginServiceTest {

    @Autowired
    private WebLoginService andRegisterService;

    @Test
    public void login() throws URISyntaxException {
        andRegisterService.login("admin.admin","jwtpass");
    }
}