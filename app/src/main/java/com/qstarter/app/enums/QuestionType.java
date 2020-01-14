package com.qstarter.app.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;

/**
 * @author peter
 * date: 2019-12-12 16:59
 **/
public enum QuestionType implements ConvertEnumFromVal<String> {
    USE("使用类"),
    GOLD_WITHDRAW("金币和提现类");


    private String descr;

    QuestionType(String descr) {
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }


    @Override
    public String getVal() {
        return name();
    }
}
