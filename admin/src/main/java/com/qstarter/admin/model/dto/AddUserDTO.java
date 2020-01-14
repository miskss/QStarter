package com.qstarter.admin.model.dto;

import com.qstarter.admin.entity.WebUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_MESSAGE;
import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_REGX;

/**
 * @author peter
 * date: 2019-09-11 09:03
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class AddUserDTO extends LoginDTO {


    @NotBlank(message = "用户手机号不能为空")
    @Pattern(regexp = PHONE_REGX, message = PHONE_MESSAGE)
    private String phoneNumber;


    @NotNull(message = "角色不能为空")
    @Size(min = 1,message = "角色不能为空")
    private List<Long> roleIds;

    private String iconUrl;


    public WebUser convertToEntity() {
        WebUser webUser = new WebUser();
        webUser.setUsername(super.getUsername());
        webUser.setPhoneNumber(this.phoneNumber);
        webUser.setIconUrl(this.iconUrl);
        return webUser;
    }

}
