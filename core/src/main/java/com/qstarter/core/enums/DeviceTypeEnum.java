package com.qstarter.core.enums;

/**
 * @author peter
 * date: 2019-12-20 08:55
 **/
public enum  DeviceTypeEnum implements ConvertEnumFromVal<String>{

    ANDROID("android"),

    IOS("ios"),

    WINPHONE("winphone"),

    //全平台推送的方式
    ALL("all");

    private String descr;

    DeviceTypeEnum(String descr) {
        this.descr = descr;
    }

    @Override
    public String getVal() {
        return descr;
    }
}
