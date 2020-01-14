package com.qstarter.admin.service;

import com.qstarter.admin.entity.WebUser;
import com.qstarter.admin.model.dto.ForgetPasswordDTO;
import com.qstarter.admin.model.vo.LoginResultVO;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.model.AccessToken;
import com.qstarter.core.thirdly_service.sms.SmsCodeSendService;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.service.AccountService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

/**
 * 网页端用户注册登录
 *
 * @author peter
 * date: 2019-09-06 09:45
 **/
@Service
public class WebLoginService {

    private AccountService accountService;
    private WebUserService webUserService;
    private SmsCodeSendService smsCodeSendService;

    public WebLoginService(AccountService accountService, WebUserService webUserService, SmsCodeSendService smsCodeSendService) {
        this.accountService = accountService;
        this.webUserService = webUserService;
        this.smsCodeSendService = smsCodeSendService;
    }

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link LoginResultVO}
     */
    public LoginResultVO login(String username, String password) {
        WebUser user = webUserService.findByUserName(username);
        OAuth2AccessToken token = accountService.getToken(user.encodeUsername(), password);

        Set<SystemRole> roles = accountService.findRolesByUserId(user.getId());

        AccessToken accessToken = AccessToken.convert(token);
        return new LoginResultVO(accessToken, roles);
    }

    /**
     * 手机号短信登录
     *
     * @param phoneNumber 手机号
     * @param smsCode     短信验证码
     * @return {@link LoginResultVO}
     */
    public LoginResultVO loginByPhoneNumber(String phoneNumber, String smsCode) {
        //验证短信
        smsCodeSendService.isMatch(phoneNumber, smsCode);
        WebUser webUser = webUserService.findByPhoneNumber(phoneNumber);
        OAuth2AccessToken token = accountService.getToken(webUser.getId());
        Set<SystemRole> roles = accountService.findRolesByUserId(webUser.getId());
        AccessToken accessToken = AccessToken.convert(token);
        return new LoginResultVO(accessToken, roles);
    }

    /**
     * 重置密码
     *
     * @param dto {@link ForgetPasswordDTO}
     */
    public void resetPassword(ForgetPasswordDTO dto) {

        WebUser userName = webUserService.findByUserName(dto.getUsername());

        if (!Objects.equals(dto.getPhoneNumber(), userName.getPhoneNumber())) {
            throw new SystemServiceException(ErrorMessageEnum.BAD_REQUEST, "用户名和手机号不匹配");
        }

        //验证短信
        smsCodeSendService.isMatch(dto.getPhoneNumber(), dto.getSmsCode());

        accountService.resetPassword(userName.getId(), dto.getPassword());
    }


    /**
     * 刷新token
     *
     * @param refreshToken {@code refresh_token}
     * @return {@link AccessToken}
     */
    public AccessToken refreshToken(String refreshToken) {
        OAuth2AccessToken token = accountService.getToken(refreshToken);
        return AccessToken.convert(token);
    }

    /**
     * 登出
     *
     * @param userId 用户id
     */
    public void logout(Long userId) {
        accountService.logout(userId);
    }

    /**
     * 修改密码
     *
     * @param userId      用户密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        webUserService.findOne(userId);
        accountService.changeUserPassword(userId, oldPassword, newPassword);
    }

    public void sendSms(String phoneNumber) {
        smsCodeSendService.sendSmsCode(phoneNumber);
    }


}
