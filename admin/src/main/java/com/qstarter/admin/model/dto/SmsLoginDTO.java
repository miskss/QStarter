package com.qstarter.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.*;

/**
 * @author peter
 * date: 2019-09-24 15:28
 **/
@ApiModel
@Data
public class SmsLoginDTO {

    @ApiModelProperty
    @Pattern(regexp = PHONE_REGX, message = PHONE_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = SMS_MESSAGE)
    private String smsCode;
}
