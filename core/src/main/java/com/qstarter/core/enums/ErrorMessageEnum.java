package com.qstarter.core.enums;

import com.qstarter.core.constant.CommonArgumentsValidConstant;

/**
 * @author peter
 * date: 2019-09-05 09:08
 **/
public enum ErrorMessageEnum {
    METHOD_NOT_ALLOWED(405, "方法不被支持"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZATION(401, "没有权限，需要认证授权"),
    SYSTEM_UNKNOWN_ERROR(500, "系统未知异常"),
    USERNAME_OR_PASSWORD_ERROR(1000, "用户名或密码不正确"),
    CONSTRAINT_VIOLATION_EXCEPTION(1001, ""),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(1002, ""),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION(1003, "http media type 类型不支持"),
    NO_SUCH_ENUM_EXCEPTION(1004, "没有找到enum类型"),
    NO_DATA_EXIST_EXCEPTION(1005, "查找的数据不存在"),
    DATA_ALREADY_EXIST_EXCEPTION(1006, "数据在数据库中已存在"),
    INVALID_REFRESH_TOKEN(1007, "refresh_token 无效"),
    IMAGE_UPLOAD_FAIL(1008, "图片上传失败"),
    SMS_CODE_IS_NOT_MATCH(1009, "短信验证码不正确"),
    CANNOT_DELETE_DATA(1016, "数据不能被删除"),
    SMS_SEND_FAIL(1017, "短信发送失败"),
    SMS_IP_BAN(1018, "短信发送太过频繁，ip已封禁，将在%d分钟后解封"),
    HTML_NOT_EXIST(1019, "页面不存在"),
    RESOURCE_NOT_EXIST(1020, "接口资源不存在"),


    APP_USER_NOT_EXIST(2000, "用户不存在"),
    APP_USER_PHONE_OR_PASSWORD_ERROR(2001, "手机号或密码不正确"),
    APP_REPEAT_REGISTERED(2002, "重复注册"),

    ALIPAY_PAY_FAIL(2020,"调起支付宝支付失败"),
    ALIPAY_QUERY_FAIL(2021,"支付宝查询订单失败"),
    WECHAT_REQUEST_FAIL(2022, "微信请求失败"),
    WXPAY_REQUEST_PAY_FAIL(2024, "微信支付请求失败"),
    WXPAY_PAY_QUERY_FAIL(2025, "微信支付请求失败"),

    FILE_SIZE_OVERSIZE(3004,"文件过大"),
    FILE_TYPE_INCORRECT(3003, "文件类型不正确"),
    APP_VERSION_EXCEPTION(3002, ""),
    PHONE_NUMBER_ALREADY_EXIST(3001, "手机号已存在"),
    TWICE_PASSWORD_NOT_MATCH_EXCEPTION(3000, CommonArgumentsValidConstant.PASSWORD_TWICE_DIFFERENT);

    ErrorMessageEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
