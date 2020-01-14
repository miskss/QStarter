package com.qstarter.app.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_MESSAGE;
import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_REGX;

/**
 * @author peter
 * date: 2019-09-27 10:47
 **/
@ApiModel
@Data
class PhoneNumberDTO {

    @NotBlank
    @Pattern(regexp = PHONE_REGX, message = PHONE_MESSAGE)
    @ApiModelProperty(value = "phoneNumber")
    private String phoneNumber;

}
