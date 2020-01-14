package com.qstarter.app.model.dto;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author peter
 * date: 2019-10-28 09:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class AppChangePasswordDTO extends SetPasswordDTO {

    @ApiModelProperty(value = "原密码", required = true)
    @NotBlank
    private String oldPassword;

    @Override
    public void validParams() {
        if (Objects.equals(getPassword(), oldPassword)) {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "新密码和原密码一致，不需要修改");
        }
        super.validParams();
    }
}
