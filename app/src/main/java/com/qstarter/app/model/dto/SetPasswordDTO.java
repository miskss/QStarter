package com.qstarter.app.model.dto;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Objects;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.*;

/**
 * @author peter
 * date: 2019-10-28 09:36
 **/
@Data
@ApiModel
public class SetPasswordDTO {

    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    @ApiModelProperty(name = "password", value = "新密码", required = true)
    private String password;

    @ApiModelProperty(required = true)
    private String confirmPassword;

    public void validParams() {

        if (!Objects.equals(password, confirmPassword)) {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "两次输入的密码不一致");
        }
    }
}
