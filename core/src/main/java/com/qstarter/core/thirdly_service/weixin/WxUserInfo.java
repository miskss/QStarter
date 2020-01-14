package com.qstarter.core.thirdly_service.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author peter
 * date: 2019-12-18 11:42
 **/
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class WxUserInfo extends ErrorMsg{
    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<String> privilege;
}
