package com.qstarter.app.controller;

import com.qstarter.app.entity.AppUser;
import com.qstarter.app.model.dto.*;
import com.qstarter.app.model.vo.AppUserVO;
import com.qstarter.app.service.AppUserService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.thirdly_service.sms.SmsCodeSendService;
import com.qstarter.security.utils.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author peter
 * date: 2019-10-30 17:15
 **/
@Api(tags = "【APP接口】用户信息")
@RestController
@RequestMapping("/api/app/user")
@Validated
public class AppUserInfoController {
    private AppUserService appUserService;
    private SmsCodeSendService smsCodeSendService;

    public AppUserInfoController(AppUserService appUserService, SmsCodeSendService smsCodeSendService) {
        this.appUserService = appUserService;
        this.smsCodeSendService = smsCodeSendService;
    }

    @PutMapping
    @ApiOperation("【需要token】更新app的用户信息")
    public GenericMsg<AppUserVO> update(@RequestBody @Valid UserDTO dto) {
        AppUser update = appUserService.update(dto.getNickname(), dto.getIconUrl());
        return GenericMsg.success(AppUserVO.fromEntity(update));
    }

    @GetMapping
    @ApiOperation("【需要token】获取app的用户信息")
    public GenericMsg<AppUserVO> get() {
        return GenericMsg.success(appUserService.getAppUserInfo());
    }

    @ApiOperation("【需要token】绑定或更换手机号")
    @PutMapping("/phone")
    public GenericMsg phoneNumber(@Valid @RequestBody SmsLoginDTO dto) {
        smsCodeSendService.isMatch(dto.getPhoneNumber(), dto.getSmsCode());
        appUserService.bindOrChangePhoneNumber(ContextHolder.getUserId(), dto.getPhoneNumber());
        return GenericMsg.success();
    }

    @ApiOperation("【需要token】绑定或更换微信")
    @GetMapping("/wx")
    public GenericMsg WX(@RequestParam @NotNull String code) {
        appUserService.bindWX(code);
        return GenericMsg.success();
    }
}
