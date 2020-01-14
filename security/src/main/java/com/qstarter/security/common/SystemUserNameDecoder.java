package com.qstarter.security.common;

/**
 * 系统名称 = 系统真实的用户名 +
 *
 * 解析出 系统真实的用户名 username
 * @author peter
 * date: 2019-09-25 09:36
 **/
public class SystemUserNameDecoder {

    public static String usernameDecoder(String systemUsername) {

        for (SystemUserNameSuffix value : SystemUserNameSuffix.values()) {

            String descr = value.getDescr();

            if (!systemUsername.contains(descr)) continue;

            return systemUsername.replaceAll(descr, "");
        }

        throw new IllegalArgumentException("unKnow system user name suffix");

    }
}
