package com.qstarter.security.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;

/**
 * @author peter
 * create: 2019-09-04 16:07
 **/
public enum ResourceType implements ConvertEnumFromVal<String> {
    PAGE,
    INTERFACE;

    @Override
    public String getVal() {
        return this.name();
    }
}
