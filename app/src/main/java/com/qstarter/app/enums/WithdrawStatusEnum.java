package com.qstarter.app.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;
import com.qstarter.core.enums.convert2db.AbstractEnumConverter;
import com.qstarter.core.enums.convert2db.PersistEnum2DB;

/**
 * @author peter
 * create: 2019-11-29 10:21
 **/
public enum WithdrawStatusEnum implements ConvertEnumFromVal<String> , PersistEnum2DB<Integer> {
    WAIT(0),
    SUCCESS(1),
    FAIL(2);

    private int code;

    WithdrawStatusEnum(int code) {
        this.code = code;
    }

    @Override
    public String getVal() {
        return name();
    }

    @Override
    public Integer getData() {
        return this.code;
    }

    public static class Converter extends AbstractEnumConverter<WithdrawStatusEnum,Integer>{
        public Converter() {
            super(WithdrawStatusEnum.class);
        }
    }


}
