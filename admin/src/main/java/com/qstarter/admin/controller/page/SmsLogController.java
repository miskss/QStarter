package com.qstarter.admin.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author peter
 * date: 2019-12-11 16:01
 **/
@Controller
public class SmsLogController {

    @GetMapping("/page/sys-log.html")
    public String sysLog() {
        return "pages/log/sys-log";
    }
}
