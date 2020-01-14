package com.qstarter.core.model;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author peter
 * date: 2019-09-06 09:29
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GenericMsg<T> extends GenericErrorMessage {


    private static final int SUCCESS_STATUS = 200;
    private static final String SUCCESS = "success";

    private T data;

    public static GenericMsg<Void> success() {
        return new GenericMsg<>(SUCCESS_STATUS, null, SUCCESS);
    }

    public static <T> GenericMsg<T> success(T data) {
        return new GenericMsg<>(SUCCESS_STATUS, data, SUCCESS);
    }

    public static GenericMsg<Void> fail(SystemServiceException e) {
        return new GenericMsg<>(e.getCode(), null, e.getMessage());
    }

    public static GenericMsg<Void> fail(ErrorMessageEnum e) {
        return new GenericMsg<>(e.getCode(), null, e.getMsg());
    }

    public static GenericMsg<Void> fail(ErrorMessageEnum e, String errorMsg) {
        return new GenericMsg<>(e.getCode(), null, errorMsg);
    }

    private GenericMsg(int status, T data, String errorMsg) {
        super(status, errorMsg);
        this.data = data;
    }
}
