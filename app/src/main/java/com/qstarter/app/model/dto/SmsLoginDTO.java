package com.qstarter.app.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.SMS_MESSAGE;

/**
 * @author peter
 * date: 2019-09-27 13:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SmsLoginDTO extends PhoneNumberDTO {

    @NotBlank(message = SMS_MESSAGE)
    private String smsCode;

}
