package com.qstarter.security.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;

/**
 * @author peter
 * date: 2019-11-07 10:55
 **/
public enum HtmlType implements ConvertEnumFromVal<String> {

    PAGE,
    FUNC;

    @Override
    public String getVal() {
        return this.name();
    }
}
