package com.qstarter.admin.model.dto;

import com.qstarter.admin.constant.ArgumentsValidConstant;
import com.qstarter.core.constant.CommonArgumentsValidConstant;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author peter
 * date: 2019-09-06 11:35
 **/
@Data
@ApiModel
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = ArgumentsValidConstant.USERNAME_LENGTH_MIN, max = ArgumentsValidConstant.USERNAME_LENGTH_MAX, message = ArgumentsValidConstant.USERNAME_VALID_MESSAGE)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = CommonArgumentsValidConstant.PASSWORD_LENGTH_MIN, max = CommonArgumentsValidConstant.PASSWORD_LENGTH_MAX, message = CommonArgumentsValidConstant.PASSWORD_VALID_MESSAGE)
    private String password;
}
