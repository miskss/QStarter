package com.qstarter.app.utils;

import com.google.common.base.Strings;
import com.qstarter.security.utils.ContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author peter
 * date: 2019-11-28 15:27
 **/
public class UrlUtils {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    public static String toHttpUrl(String url){

        if (Strings.isNullOrEmpty(url)) return url;

        if (url.startsWith(HTTP) || url.startsWith(HTTPS)){
            return url;
        }
        HttpServletRequest request = ContextHolder.getRequest();
        if (request == null) return url;

        int serverPort = request.getServerPort();
        String serverName = request.getServerName();
        String requestUrl = request.getRequestURL().toString();

        if (requestUrl.startsWith(HTTP)){
            return String.format("%s://%s:%s%s",HTTP,serverName,serverPort,url);
        }else {
            return String.format("%s://%s:%s%s",HTTPS,serverName,serverPort,url);
        }
    }

}
