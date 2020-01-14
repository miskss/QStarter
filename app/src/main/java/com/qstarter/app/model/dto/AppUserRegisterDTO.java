package com.qstarter.app.model.dto;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.*;

/**
 * @author peter
 * date: 2019-09-27 09:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class AppUserRegisterDTO extends PhoneNumberDTO {

    @NotBlank(message = SMS_MESSAGE)
    private String smsCode;

    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @NotBlank(message = PASSWORD_TWICE_DIFFERENT)
    private String confirmPassword;


    public void validParams() {

        if (!Objects.equals(password, confirmPassword)) {
            throw new SystemServiceException(ErrorMessageEnum.TWICE_PASSWORD_NOT_MATCH_EXCEPTION);
        }

    }

}
