package com.qstarter.admin.model.dto;

import com.google.common.base.Strings;
import com.qstarter.admin.constant.ArgumentsValidConstant;
import com.qstarter.admin.entity.WebUser;
import com.qstarter.core.constant.CommonArgumentsValidConstant;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author peter
 * date: 2019-09-11 10:37
 **/
@Data
@ApiModel
public class UpdateUserDTO {
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    @Length(min = ArgumentsValidConstant.USERNAME_LENGTH_MIN, max = ArgumentsValidConstant.USERNAME_LENGTH_MAX, message = ArgumentsValidConstant.USERNAME_VALID_MESSAGE)
    private String username;

    @NotBlank(message = "用户手机号不能为空")
    @Pattern(regexp = CommonArgumentsValidConstant.PHONE_REGX, message = "请输入正确的手机号")
    private String phoneNumber;

    private String password;

    @NotNull
    @Size(min = 1)
    private List<Long> roleIds;

    private String iconUrl;

    public void validParams() {
        if (!Strings.isNullOrEmpty(password)) {
            int length = password.length();
            if (length < CommonArgumentsValidConstant.PASSWORD_LENGTH_MIN || length > CommonArgumentsValidConstant.PASSWORD_LENGTH_MAX) {
                throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, CommonArgumentsValidConstant.PASSWORD_VALID_MESSAGE);
            }
        }
    }


    public WebUser convertToEntity() {
        WebUser webUser = new WebUser();
        webUser.initId(this.userId);
        webUser.setUsername(this.username);
        webUser.setPhoneNumber(this.phoneNumber);
        webUser.setIconUrl(this.iconUrl);
        return webUser;
    }
}
