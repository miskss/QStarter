package com.qstarter.admin.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author peter
 * date: 2019-12-09 09:09
 **/
@Controller
public class VipUserController {


    @GetMapping("/page/vip-user.html")
    public String vipUser() {
        return "pages/vip/user";
    }


}
