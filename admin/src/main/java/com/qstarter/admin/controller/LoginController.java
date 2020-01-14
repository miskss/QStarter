package com.qstarter.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.admin.constant.HttpSessionAttrKey;
import com.qstarter.admin.event.LoginLogHandler;
import com.qstarter.admin.model.dto.ForgetPasswordDTO;
import com.qstarter.admin.model.dto.LoginDTO;
import com.qstarter.admin.model.dto.SmsLoginDTO;
import com.qstarter.admin.model.vo.LoginResultVO;
import com.qstarter.admin.service.WebLoginService;
import com.qstarter.core.model.AccessToken;
import com.qstarter.core.model.GenericMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_MESSAGE;
import static com.qstarter.core.constant.CommonArgumentsValidConstant.PHONE_REGX;

/**
 * @author peter
 * date: 2019-09-06 09:27
 **/
@Api(tags = {"【后台网页】登录"})
@RestController
@RequestMapping("/api/public/web")
@Validated
public class LoginController {

    private WebLoginService webLoginService;

    public LoginController(WebLoginService webLoginService) {
        this.webLoginService = webLoginService;
    }

    @ApiOperation("用户名密码登录")
    @PostMapping("/login")
    @MessageLog(value = "登录",handler = LoginLogHandler.class)
    public GenericMsg<LoginResultVO> login(@Valid @RequestBody LoginDTO dto, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        LoginResultVO vo = webLoginService.login(dto.getUsername(), dto.getPassword());
        request.getSession().setAttribute(HttpSessionAttrKey.USER_ROLES, vo.getRoles());
        return GenericMsg.success(vo);
    }

    @ApiOperation("刷新token")
    @GetMapping("/refresh-token")
    public GenericMsg<AccessToken> refresh(@RequestParam String refreshToken) {
        AccessToken accessToken = webLoginService.refreshToken(refreshToken);
        return GenericMsg.success(accessToken);
    }

    @ApiOperation("发送短信")
    @GetMapping("/sms")
    public GenericMsg sendSms(@RequestParam @Pattern(regexp = PHONE_REGX, message = PHONE_MESSAGE) String phoneNumber) {
        webLoginService.sendSms(phoneNumber);
        return GenericMsg.success();
    }

    @ApiOperation("手机短信登录")
    @PostMapping("/login-by-phone-number")
    public GenericMsg<LoginResultVO> login(@Valid @RequestBody SmsLoginDTO dto) {
        return GenericMsg.success(webLoginService.loginByPhoneNumber(dto.getPhoneNumber(), dto.getSmsCode()));
    }

    @ApiOperation("忘记密码")
    @PostMapping("/forget-password")
    public GenericMsg forgetPassword(@Valid @RequestBody ForgetPasswordDTO dto) {
        dto.validParams();
        webLoginService.resetPassword(dto);
        return GenericMsg.success();
    }


}
