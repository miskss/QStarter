package com.qstarter.admin.model.dto;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.*;

/**
 * @author peter
 * date: 2019-09-12 09:41
 **/
@Data
@ApiModel
public class ChangePasswordDTO {

    @NotBlank(message = "原密码不能为空")
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    private String oldPassword;

    @NotBlank(message = "密码不能为空")
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX, message = PASSWORD_VALID_MESSAGE)
    private String confirmPassword;


    public void validParams() {

        if (Objects.equals(oldPassword, newPassword)) {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "新密码和原密码一致不需要修改");
        }
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "两次输入的密码不一致");
        }
    }


}
