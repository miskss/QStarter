package com.qstarter.admin.controller.page;

import com.qstarter.admin.constant.HttpSessionAttrKey;
import com.qstarter.admin.model.vo.HasHtmlTreeVO;
import com.qstarter.admin.service.RoleService;
import com.qstarter.admin.service.SystemHtmlResourceService;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.utils.ContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * @author peter
 * date: 2019-11-14 10:40
 **/
@Controller
@SuppressWarnings("unchecked")
public class IndexController {

    private RoleService roleService;
    private SystemHtmlResourceService htmlResourceService;

    public IndexController(RoleService roleService, SystemHtmlResourceService htmlResourceService) {
        this.roleService = roleService;
        this.htmlResourceService = htmlResourceService;
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) {

        HttpSession session = ContextHolder.getRequest().getSession();
        Object roles = session.getAttribute(HttpSessionAttrKey.USER_ROLES);
        if (roles == null) {
            return "redirect:/login.html";
        }

        Set<String> roleSet = (Set<String>) roles;
        Set<SystemRole> byRoleNames = roleService.findByRoleNames(roleSet);

        List<HasHtmlTreeVO> hasHtmlTreeVOS = htmlResourceService.checkRoleHtml(byRoleNames);
        model.addAttribute("htmls", hasHtmlTreeVOS);
        session.setAttribute(HttpSessionAttrKey.HTML_AUTHORITY, hasHtmlTreeVOS);
        return "index";
    }

    @GetMapping("/page/system-user.html")
    public String string11() {
        return "pages/users";
    }


    @GetMapping("/page/system-role.html")
    public String systemRole() {
        return "pages/role";
    }


}
