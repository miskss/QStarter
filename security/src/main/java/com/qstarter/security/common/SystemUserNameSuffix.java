package com.qstarter.security.common;

/**
 * @author peter
 * create: 2019-09-25 09:34
 **/
public enum SystemUserNameSuffix {

    WEB("@WEB"),
    APP("@APP");


    SystemUserNameSuffix(String descr) {
        this.descr = descr;
    }

    private String descr;

    public String getDescr() {
        return descr;
    }
}
