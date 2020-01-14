package com.qstarter.security.utils;

import com.google.common.base.Strings;
import com.qstarter.security.exception.UnAuthorizationException;
import com.qstarter.security.model.UserDetail;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author peter
 * date: 2019-09-09 17:26
 **/
public final class ContextHolder {

    public static String requestIp() {
        HttpServletRequest request = getRequest();
        if (request == null) return "";
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (!Strings.isNullOrEmpty(remoteAddr))
            return remoteAddr;
        return request.getRemoteAddr();
    }


    /**
     * 获取当前请求的 的 权限
     *
     * @return {@link Authentication}
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前请求
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return null;
        return attributes.getRequest();
    }


    public static <T> void setRequestCache(String key, T data) {
        HttpServletRequest request = getRequest();
        if (request == null) return;
        request.setAttribute(getCacheKey(key), data);
    }

    public static <T> T getRequestCache(String key) {
        HttpServletRequest request = getRequest();

        if (request == null) return null;

        return (T) request.getAttribute(getCacheKey(key));
    }

    public static void clearCache(String key) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return;
        }
        request.removeAttribute(getCacheKey(key));
    }

    private static String getCacheKey(String key) {
        return "request-cache-" + key;
    }


    /**
     * 从access token 中获取 用户id
     *
     * @return userId
     */
    public static Long getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizationException();
        }

        UserDetail token = (UserDetail) authentication.getPrincipal();

        return token.getSystemUserId();
    }

    public static Long getUserIdOrNull() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        UserDetail token = (UserDetail) authentication.getPrincipal();
        return token.getSystemUserId();
    }

}
