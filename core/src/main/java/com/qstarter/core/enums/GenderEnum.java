package com.qstarter.core.enums;

import com.qstarter.core.enums.convert2db.AbstractEnumConverter;
import com.qstarter.core.enums.convert2db.PersistEnum2DB;

/**
 * @author peter
 * date: 2019-11-20 10:37
 **/
public enum GenderEnum implements ConvertEnumFromVal<Integer>, PersistEnum2DB<Integer> {

    UNKNOWN(0,"未知"),
    MAN(1,"男"),
    WOMAN(2,"女");

    int code;

    String descr;

    GenderEnum(int code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }

    @Override
    public Integer getVal() {
        return this.code;
    }

    @Override
    public Integer getData() {
        return this.code;
    }

    public static class GendersConverter extends AbstractEnumConverter<GenderEnum,Integer>{
        public GendersConverter() {
            super(GenderEnum.class);
        }
    }

}
