package com.qstarter.app.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author peter
 * date: 2019-10-30 17:16
 **/
@Data
@ApiModel
public class UserDTO {

    @ApiModelProperty
    @Size(min = 1, max = 30, message = "昵称的长度在1~30")
    private String nickname;

    @ApiModelProperty
    private String iconUrl;
}
