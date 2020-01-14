package com.qstarter.admin.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author peter
 * date: 2019-11-18 11:02
 **/
@Controller
public class ResourcePageController {

    @GetMapping("/resource/index.html")
    public String index(){
        return "pages/resource";
    }

}
