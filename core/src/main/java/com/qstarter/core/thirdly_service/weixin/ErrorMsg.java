package com.qstarter.core.thirdly_service.weixin;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * @author peter
 * date: 2019-12-18 13:51
 **/
@Data
public class ErrorMsg {
    String errcode;
    String errmsg;
    protected boolean success(){
        return Strings.isNullOrEmpty(errcode) && Strings.isNullOrEmpty(errmsg);
    }

    protected String error(){
        return toString();
    }
}
