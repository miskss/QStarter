package com.qstarter.admin.controller.page;

import com.qstarter.app.entity.AppVersion;
import com.qstarter.app.service.AppVersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 菜单管理页面
 *
 * @author peter
 * date: 2019-11-19 08:58
 **/
@Controller
public class MenuPageController {
    private AppVersionService appVersionService;

    public MenuPageController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @GetMapping("/page/menu.html")
    public String index() {
        return "pages/menu";
    }

    @GetMapping("/page/app-version.html")
    public String appVersion(Model model) {
        AppVersion androidLatestOne = appVersionService.findAndroidLatestOne();
        model.addAttribute("android", androidLatestOne);
        return "pages/operation/app-version";
    }

}
