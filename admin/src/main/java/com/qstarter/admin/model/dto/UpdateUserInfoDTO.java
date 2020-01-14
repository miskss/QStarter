package com.qstarter.admin.model.dto;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author peter
 * date: 2019-09-23 16:40
 **/
@Data
@ApiModel
public class UpdateUserInfoDTO {
    @ApiModelProperty(name = "手机号")
    private String phoneNumber;

    @ApiModelProperty(name = "头像")
    private String iconUrl;

    @ApiModelProperty(name = "短信验证码，当需要修改手机号时则必须传")
    private String smsCode;

    public void validParams() {
        if (!StringUtils.hasText(phoneNumber) && !StringUtils.hasText(iconUrl)) {
            throw new SystemServiceException(ErrorMessageEnum.BAD_REQUEST, "手机号和头像不能同时为空");
        }

        if (StringUtils.hasText(phoneNumber) && StringUtils.isEmpty(smsCode)) {
            throw new SystemServiceException(ErrorMessageEnum.SMS_CODE_IS_NOT_MATCH);
        }
    }

}
