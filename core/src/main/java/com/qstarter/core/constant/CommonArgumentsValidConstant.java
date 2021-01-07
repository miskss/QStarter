package com.qstarter.core.constant;

/**
 * @author peter
 * create: 2019-09-24 15:06
 **/
public interface CommonArgumentsValidConstant {

    String PHONE_REGX = "^[1][0-9]{10}$";
    String ALL_NUMBER_REGX = "^[0-9]*$";
    String PHONE_MESSAGE = "手机号不正确";
    String PHONE_NOT_EMPTY = "手机号不能为空";

    String SMS_MESSAGE = "短信验证码不能为空";

    int PASSWORD_LENGTH_MIN = 6;
    int PASSWORD_LENGTH_MAX = 32;
    String PASSWORD_VALID_MESSAGE = "密码必须在" + PASSWORD_LENGTH_MIN + "~" + PASSWORD_LENGTH_MAX + "之间";
    String PASSWORD_ERROR_MSG = "密码不正确";
    String PASSWORD_TWICE_DIFFERENT = "两次的密码不一致";

}
