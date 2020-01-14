package com.qstarter.core.thirdly_service.payment.ex;

/**
 * @author peter
 * date: 2020-01-13 10:26
 **/
public class PayCallbackHandlerFailException extends RuntimeException {
    public PayCallbackHandlerFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
