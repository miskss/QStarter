package com.qstarter.core.thirdly_service.payment.ex;

/**
 * @author peter
 * date: 2020-01-13 09:50
 **/
public class PayServiceException extends RuntimeException {

    public PayServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
