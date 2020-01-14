package com.qstarter.security.enums;

import com.qstarter.core.enums.ConvertEnumFromVal;

/**
 * 资源操作的权限 ，增删改查
 *
 * @author peter
 * create: 2019-09-04 16:05
 **/
public enum OperationEnum implements ConvertEnumFromVal<String> {
    POST,
    GET,
    PUT,
    DELETE;

    @Override
    public String getVal() {
        return this.name();
    }
}
