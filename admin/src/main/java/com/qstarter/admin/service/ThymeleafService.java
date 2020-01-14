package com.qstarter.admin.service;

import com.google.common.collect.Lists;
import com.qstarter.admin.constant.HttpSessionAttrKey;
import com.qstarter.admin.model.vo.HasHtmlTreeVO;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.utils.ContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-12-05 09:56
 **/
@Component("thymeleafService")
@SuppressWarnings("Unckecked")
public class ThymeleafService {

    public boolean isSuperAdmin() {
        Object roles = ContextHolder.getRequest().getSession().getAttribute(HttpSessionAttrKey.USER_ROLES);
        if (roles == null) return false;
        Set<String> roleSet = (Set<String>) roles;
        return RoleInSystem.containSuperAdmin(roleSet);
    }


    public boolean hasFuncAuthority(String authorityName) {
        Object attribute = ContextHolder.getRequest().getSession().getAttribute(HttpSessionAttrKey.HTML_AUTHORITY);
        if (attribute == null) {
            return false;
        }
        List<HasHtmlTreeVO> hasHtmlTreeVOS = (List<HasHtmlTreeVO>) attribute;

        return hasHtmlTreeVOS.stream().map(this::convert)
                .flatMap(Collection::stream)
                .anyMatch(o -> Objects.equals(o, authorityName));
    }


    public List<String> convert(HasHtmlTreeVO vo) {
        if (!vo.isOwn()) {
            return Lists.newArrayList();
        }
        List<HasHtmlTreeVO> children = vo.getChildren();
        if (children == null || children.isEmpty()) {
            return Lists.newArrayList(vo.getHtmlName());
        }

        return children.stream().map(this::convert).flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
