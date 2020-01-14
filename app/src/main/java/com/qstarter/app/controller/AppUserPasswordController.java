package com.qstarter.app.controller;

import com.qstarter.app.model.dto.AppChangePasswordDTO;
import com.qstarter.app.model.dto.SetPasswordDTO;
import com.qstarter.app.service.AppUserService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.security.utils.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author peter
 * date: 2019-10-28 09:34
 **/
@Api(tags = "【APP接口】设置密码和修改密码，退出登录")
@RestController
@RequestMapping("/api/app")
public class AppUserPasswordController {

    private AppUserService appUserService;

    public AppUserPasswordController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/logout")
    @ApiOperation("【需要token】退出登录")
    public GenericMsg logout() {
        appUserService.logout(ContextHolder.getUserId());
        return GenericMsg.success();
    }

    @PutMapping("/password")
    @ApiOperation("【需要token】短信登录后设置密码")
    public GenericMsg setPassword(@RequestBody @Valid SetPasswordDTO setPasswordDTO) {

        setPasswordDTO.validParams();

        Long userId = ContextHolder.getUserId();

        appUserService.setPassword(userId, setPasswordDTO.getPassword());

        return GenericMsg.success();
    }

    @PutMapping("/password/change")
    @ApiOperation("【需要token】用户修改密码，修改成功后原token和refresh_token 都会失效")
    public GenericMsg changePassword(@RequestBody @Valid AppChangePasswordDTO dto) {

        dto.validParams();

        Long userId = ContextHolder.getUserId();

        appUserService.changePassword(userId, dto.getOldPassword(), dto.getPassword());

        return GenericMsg.success();
    }
}
