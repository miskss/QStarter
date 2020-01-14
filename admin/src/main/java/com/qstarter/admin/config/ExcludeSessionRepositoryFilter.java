package com.qstarter.admin.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author peter
 * date: 2019-12-18 08:48
 **/
@Component
@Order(Integer.MIN_VALUE)
public class ExcludeSessionRepositoryFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getServletPath();
        boolean contains = url.startsWith("/api/app") || url.contains("/api/public/app");
        boolean statistic = url.contains("statistic");
        boolean equals = url.equals("/api/web/vip/user");
        /* here goes your logic to exclude the session repository filter, probably depending on the request uri */
        if (contains || statistic || equals) {
            request.setAttribute("org.springframework.session.web.http.SessionRepositoryFilter.FILTERED", Boolean.TRUE);
        }
        filterChain.doFilter(request, response);
    }
}
