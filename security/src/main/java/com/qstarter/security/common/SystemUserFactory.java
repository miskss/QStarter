package com.qstarter.security.common;

import com.qstarter.core.utils.PasswordEncoderUtils;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.entity.SystemUser;

import java.util.Set;

/**
 * @author peter
 * date: 2019-09-06 10:35
 **/
public class SystemUserFactory {

    public static SystemUser createSystemUser(SystemUserNameEncoder encoder, Set<SystemRole> systemRoles) {
        return new SystemUser(encoder.encodeUsername(), null, systemRoles);
    }


    public static SystemUser createSystemUser(SystemUserNameEncoder encoder, String rawPassword, Set<SystemRole> systemRoles) {
        String password = PasswordEncoderUtils.encodePassword(rawPassword);
        return new SystemUser(encoder.encodeUsername(), password, systemRoles);
    }
}
