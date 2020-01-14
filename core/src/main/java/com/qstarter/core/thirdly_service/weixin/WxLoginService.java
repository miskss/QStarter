package com.qstarter.core.thirdly_service.weixin;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author peter
 * date: 2019-12-18 11:41
 **/
@Component
@Slf4j
public class WxLoginService {

    private static final String ACCESS_URL_TEMPLATE ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String USER_INFO_URL_TEMPLATE = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";




    private WeiXinLoginConfig config;

    public WxLoginService(WeiXinLoginConfig config) {
        this.config = config;
    }


    public WxAccessToken getWxToken(String code){
        String url = String.format(ACCESS_URL_TEMPLATE,config.getAppId(),config.getSecret(),code);
        WxAccessToken wxAccessToken = HttpUtils.get(url, null, WxAccessToken.class, null);
        if (wxAccessToken == null){
            log.error("微信access_token请求失败");
            throw new SystemServiceException(ErrorMessageEnum.WECHAT_REQUEST_FAIL);
        } else if (!wxAccessToken.success()) {
            log.error("微信access_token获取失败，错误信息【{}】",wxAccessToken.error());
            throw new SystemServiceException(ErrorMessageEnum.WECHAT_REQUEST_FAIL);
        }
        return wxAccessToken;
    }


    public WxUserInfo getWxUserInfo(String openId,String access_token){
        String url = String.format(USER_INFO_URL_TEMPLATE,access_token,openId);
        WxUserInfo wxUserInfo = HttpUtils.get(url, null, WxUserInfo.class, null);
        if (wxUserInfo == null){
            log.error("微信用户信息请求失败");
            throw new SystemServiceException(ErrorMessageEnum.WECHAT_REQUEST_FAIL);
        } else if (!wxUserInfo.success()) {
            log.error("微信用户信息获取失败，错误信息【{}】",wxUserInfo.error());
            throw new SystemServiceException(ErrorMessageEnum.WECHAT_REQUEST_FAIL);
        }
        return wxUserInfo;
    }

    public WxUserInfo getWxUserInfo(String code){
        WxAccessToken wxToken = getWxToken(code);
        return getWxUserInfo(wxToken.getOpenid(),wxToken.getAccess_token());
    }
}
