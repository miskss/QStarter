package com.qstarter.core.thirdly_service.weixin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class WxLoginServiceTest {

    @Autowired
    private WxLoginService wxLoginService;


    @Test
    public void getWxToken() {
        WxAccessToken wxToken = wxLoginService.getWxToken("0717NXIO15Nlb91Hp9LO1tzdJO17NXIW");
        System.out.println(wxToken);
    }

    @Test
    public void getWxUserInfo() {
        WxUserInfo wxUserInfo = wxLoginService.getWxUserInfo("oPIi7v3GgPbUwNU5mwH-5fRxOKi4", "28_TVbeC90EZuOByWZJ9V4Dk4AVdCDStll1hckl5-FmOPxJ3TumqzguoQsBzdthUTmt29xylIFGbQGMJ8GOmImu0rBizk7tYRVWYolbxzCPk6s");
        Assertions.assertNotNull(wxUserInfo);
        System.out.println(wxUserInfo);

    }

    @Test
    public void getWxUserInfo1() {
        WxUserInfo wxUserInfo = wxLoginService.getWxUserInfo("011YnsG62qbXdR0rGiG62ED8G62YnsGy");
        Assertions.assertNotNull(wxUserInfo);
        System.out.println(wxUserInfo);
    }
}