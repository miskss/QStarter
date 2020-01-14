package com.qstarter.admin.enums;

/**
 * @author peter
 * date: 2019-09-24 10:01
 **/
public enum LogActionTypeEnum {

    ADD("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    QUERY("查询");


    private String descr;

    LogActionTypeEnum(String descr) {
        this.descr = descr;
    }


    public String getDescr() {
        return descr;
    }

}
