package com.qstarter.core.model;

import lombok.Data;

/**
 * @author peter
 * date: 2019-09-05 09:05
 **/
@Data
public class GenericErrorMessage {

    private int status;

    private String errorMsg;


    public GenericErrorMessage(int status, String errorMsg) {

        this.status = status;
        this.errorMsg = errorMsg;
    }


}
