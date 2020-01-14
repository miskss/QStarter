package com.qstarter.security.enums;

import java.util.Collection;
import java.util.Objects;

/**
 * 系统内部角色的划分
 *
 * @author peter
 * create: 2019-09-09 09:58
 **/
public enum RoleInSystem {
    /**
     * 网页
     */
    WEB,
    APP,
    //超级用户
    SUPER_ADMIN;


    public static boolean isSuperAdmin(String roleName) {
        return Objects.equals(roleName, SUPER_ADMIN.name());
    }

    /**
     * 是否是系统的 基础角色
     *
     * @param roleName 角色名
     * @return true 是基础角色 ，false 不是基础角色
     */
    public static boolean contains(String roleName) {
        for (RoleInSystem value : RoleInSystem.values()) {
            String name = value.name();
            if (name.equals(roleName)) return true;
        }
        return false;
    }


    public static boolean notContainsNormalRole(String roleName) {

        if (RoleInSystem.APP.name().equals(roleName)) return false;

        if (RoleInSystem.WEB.name().equals(roleName)) return false;

        return true;
    }

    public static boolean containSuperAdmin(Collection<String> roles) {
        String superAdmin = RoleInSystem.SUPER_ADMIN.name();
        for (String role : roles) {
            if (Objects.equals(superAdmin, role)) return true;
        }

        return false;
    }
}
