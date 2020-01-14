package com.qstarter.app.controller;

import com.qstarter.app.entity.AppUser;
import com.qstarter.app.model.dto.AppUserRegisterDTO;
import com.qstarter.app.model.dto.LoginDTO;
import com.qstarter.app.model.dto.SmsLoginDTO;
import com.qstarter.app.service.AppUserService;
import com.qstarter.core.model.AccessToken;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.thirdly_service.sms.SmsCodeSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_MESSAGE;
import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_REGX;

/**
 * app用户登录注册
 *
 * @author peter
 * date: 2019-09-27 09:45
 **/
@RestController
@RequestMapping("/api/public/app")
@Api(tags = "【APP接口】注册登录,忘记密码")
public class AppUserLoginController {

    private SmsCodeSendService smsService;
    private AppUserService appUserService;

    public AppUserLoginController(SmsCodeSendService smsService, AppUserService appUserService) {
        this.smsService = smsService;
        this.appUserService = appUserService;
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public GenericMsg register(@Valid @RequestBody AppUserRegisterDTO dto) {
        dto.validParams();
        //验证短信验证码
        smsService.isMatch(dto.getPhoneNumber(), dto.getSmsCode());
        appUserService.registerAppUser(dto.getPhoneNumber(), dto.getPassword());
        return GenericMsg.success();
    }

    @PostMapping("/login")
    @ApiOperation("手机号密码登录")
    public GenericMsg<AccessToken> login(@Valid @RequestBody LoginDTO dto) {
        return GenericMsg.success(appUserService.login(dto.getPhoneNumber(), dto.getPassword()));
    }


    @PostMapping("/login-sms")
    @ApiOperation("短信登录注册")
    public GenericMsg<AccessToken> loginBySms(@Valid @RequestBody SmsLoginDTO dto) {
        //验证短信
        smsService.isMatch(dto.getPhoneNumber(), dto.getSmsCode());
        AppUser appUserElseRegister = appUserService.getAppUserElseRegister(dto.getPhoneNumber());
        return GenericMsg.success(appUserService.login(appUserElseRegister));
    }

    @GetMapping("/login-wx")
    @ApiOperation("微信注册登录")
    public GenericMsg<AccessToken> loginByWx(@RequestParam @NotNull String code) {
        AppUser appUser = appUserService.getAppUserElseRegisterByOpenId(code);
        return GenericMsg.success(appUserService.login(appUser));
    }


    @ApiOperation("忘记密码（重置密码）")
    @PutMapping("/reset-password")
    public GenericMsg resetPassword(@Valid @RequestBody AppUserRegisterDTO dto) {
        dto.validParams();
        appUserService.resetPassword(dto.getPhoneNumber(), dto.getPassword(), () -> smsService.isMatch(dto.getPhoneNumber(), dto.getSmsCode()));
        return GenericMsg.success();
    }

    @ApiOperation("发送短信")
    @GetMapping("/sms")
    public GenericMsg sendSms(@RequestParam @Pattern(regexp = PHONE_REGX, message = PHONE_MESSAGE) String phoneNumber) {
        smsService.sendSmsCode(phoneNumber);
        return GenericMsg.success();
    }

}
