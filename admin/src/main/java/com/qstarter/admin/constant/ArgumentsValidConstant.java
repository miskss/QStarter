package com.qstarter.admin.constant;

/**
 * @author peter
 * create: 2019-09-11 14:44
 **/
public interface ArgumentsValidConstant {

    int USERNAME_LENGTH_MIN = 1;
    int USERNAME_LENGTH_MAX = 32;
    String USERNAME_VALID_MESSAGE = "用户名必须在" + USERNAME_LENGTH_MIN + "~" + USERNAME_LENGTH_MAX + "之间";
    String USERNAME_NOT_EMPTY = "用户名不能为空";


}
