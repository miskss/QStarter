package com.qstarter.core.utils;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author peter
 * date: 2020-01-09 13:51
 **/
public class RemoteIpHelper {

    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) return "";
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (!Strings.isNullOrEmpty(remoteAddr)) {
            String split = ",";
            return remoteAddr.contains(split) ? remoteAddr.split(split)[0] : remoteAddr;
        }
        String realIp = request.getHeader("X-REAL-IP");
        if (!Strings.isNullOrEmpty(realIp)) {
            String split = ",";
            return realIp.contains(split) ? remoteAddr.split(split)[0] : remoteAddr;
        }
        String ip = request.getRemoteAddr();

        if (Objects.equals(ip, "0:0:0:0:0:0:0:1")) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                ip = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

}
