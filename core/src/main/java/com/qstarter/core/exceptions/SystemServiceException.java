package com.qstarter.core.exceptions;

import com.qstarter.core.enums.ErrorMessageEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author peter
 * date: 2019-09-05 10:34
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemServiceException extends RuntimeException {

    private int code;

    public SystemServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public SystemServiceException(ErrorMessageEnum errorMessageEnum) {
        this(errorMessageEnum.getCode(), errorMessageEnum.getMsg());
    }

    public SystemServiceException(ErrorMessageEnum errorMessageEnum,String errorMsg) {
        this(errorMessageEnum.getCode(), errorMsg);
    }

    public SystemServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

}
