package com.qstarter.app.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;
import com.qstarter.core.enums.convert2db.AbstractEnumConverter;
import com.qstarter.core.enums.convert2db.PersistEnum2DB;

/**
 * @author peter
 * create: 2019-11-29 10:16
 **/
public enum TransferTypeEnum implements ConvertEnumFromVal<Integer>, PersistEnum2DB<Integer> {
    ALIPAY(0, "支付宝转账"),
    BANK(1, "银行转账");

    private int code;

    private String descr;


    TransferTypeEnum(int code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    public int getCode() {
        return code;
    }

    public String getDescr() {
        return descr;
    }

    @Override
    public Integer getVal() {
        return getCode();
    }

    @Override
    public Integer getData() {
        return getCode();
    }

    public static class Converter extends AbstractEnumConverter<TransferTypeEnum, Integer> {
        public Converter() {
            super(TransferTypeEnum.class);
        }
    }
}
