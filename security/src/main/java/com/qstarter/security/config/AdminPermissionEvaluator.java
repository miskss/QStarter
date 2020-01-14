package com.qstarter.security.config;

import com.qstarter.security.service.ResourcePool;
import com.qstarter.security.service.RolePool;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.utils.ContextHolder;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义 网页后台权限的拦截
 * 使用{@code @PreAuthorize("hasPermission('','')")}
 *
 * @author peter
 * date: 2019-09-06 17:22
 **/
@Component
public class AdminPermissionEvaluator implements PermissionEvaluator {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return hasPermission(authentication);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication);
    }

    private boolean hasPermission(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //过滤掉通用的权限角色
        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(RoleInSystem::notContainsNormalRole)
                .collect(Collectors.toSet());
        if (roles.isEmpty()) return false;

        //是否包含超级管理员角色，如果有直接放行，否则下一步判断
        boolean isSuperAdmin = RoleInSystem.containSuperAdmin(roles);
        if (isSuperAdmin) return true;

        //当前请求的资源
        HttpServletRequest request = ContextHolder.getRequest();
        if (request == null) return false;
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        //获取该角色下的资源列表，查看是否有包含当前请求的资源
        return roles.stream()
                //获取角色所拥有的资源
                .map(RolePool::getResourcesByRoleName)
                //过滤掉 没有资源的角色
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .distinct()
                //将资源id转化为资源
                .map(ResourcePool::get)
                //过滤掉不存在的资源
                .filter(Objects::nonNull)
                //其中有一条匹配即可
                .anyMatch(resource -> isMatch(resource, requestURI, method));
    }

    private boolean isMatch(SystemResource resource, String requestUri, String requestMethod) {
        boolean match = antPathMatcher.match(resource.getResourceUrl(), requestUri);
        return match && requestMethod.equals(resource.getOperation().getVal());
    }

}
