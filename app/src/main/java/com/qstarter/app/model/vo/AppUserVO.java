package com.qstarter.app.model.vo;

import com.google.common.base.Strings;
import com.qstarter.app.entity.AppUser;
import com.qstarter.app.utils.UrlUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author peter
 * date: 2019-10-30 17:22
 **/
@Data
@ApiModel
@Builder
public class AppUserVO {

    @ApiModelProperty("是否已绑定手机")
    private boolean hasBindPhone;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("头像")
    private String iconUrl;
    @ApiModelProperty
    private boolean hasBindWX;
    @ApiModelProperty("用户信息")
    private String phoneNumber;


    public static AppUserVO fromEntity(AppUser appUser) {
        return AppUserVO.builder()
                .hasBindPhone(!Strings.isNullOrEmpty(appUser.getPhoneNumber()))
                .iconUrl(UrlUtils.toHttpUrl(appUser.getIconUrl()))
                .nickname(appUser.getNickname())
                .hasBindWX(!Strings.isNullOrEmpty(appUser.getOpenId()))
                .build();
    }
}
