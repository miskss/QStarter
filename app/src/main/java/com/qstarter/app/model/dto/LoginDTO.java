package com.qstarter.app.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.*;

/**
 * @author peter
 * date: 2019-09-27 10:48
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginDTO extends PhoneNumberDTO {

    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    @ApiModelProperty
    private String password;
}
