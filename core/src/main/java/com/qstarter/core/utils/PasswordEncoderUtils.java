package com.qstarter.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 系统中密码服务的工具类
 *
 * @author peter
 * date: 2019-09-04 16:57
 **/
@Component
public final class PasswordEncoderUtils {

    private static PasswordEncoder passwordEncoder;

    public PasswordEncoderUtils(PasswordEncoder passwordEncoder) {
        PasswordEncoderUtils.passwordEncoder = passwordEncoder;
    }

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean isMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        String all = "11";
        System.out.println(new BCryptPasswordEncoder().encode(all));
    }
}
