package com.qstarter.security.common;

/**
 * @author peter
 * date: 2019-09-06 10:36
 **/
public interface SystemUserNameEncoder {

    /**
     * 处理 web 模块和 app 模块下的用户名称
     *
     * @return String
     */
    String encodeUsername();

}
