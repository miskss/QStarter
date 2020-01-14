package com.qstarter.admin.controller;

import com.qstarter.admin.entity.WebUser;
import com.qstarter.admin.model.dto.ChangePasswordDTO;
import com.qstarter.admin.model.dto.UpdateUserInfoDTO;
import com.qstarter.admin.model.vo.WebUserVO;
import com.qstarter.admin.service.WebLoginService;
import com.qstarter.admin.service.WebUserService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.utils.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author peter
 * date: 2019-09-09 17:23
 **/
@Api(tags = "【后台网页】用户信息接口")
@RestController
@RequestMapping("/api/web/user")
public class WebUserController {

    private WebUserService webUserService;
    private WebLoginService webLoginService;

    public WebUserController(WebUserService webUserService, WebLoginService webLoginService) {
        this.webLoginService = webLoginService;
        this.webUserService = webUserService;
    }

    @ApiOperation("获取用户信息")
    @GetMapping
    public GenericMsg<WebUserVO> info() {

        Long userId = ContextHolder.getUserId();

        Set<SystemRole> rolesByUserId = webUserService.findRolesByUserId(userId);

        WebUser webUser = webUserService.findOne(userId);

        return GenericMsg.success(WebUserVO.fromEntity(webUser, rolesByUserId));
    }

    @ApiOperation("用户修改密码")
    @PostMapping("/password")
    public GenericMsg changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        //参数验证
        dto.validParams();
        Long userId = ContextHolder.getUserId();
        webLoginService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return GenericMsg.success();
    }

    @ApiOperation("用户修改用户信息")
    @PutMapping
    public GenericMsg<WebUserVO> updateUserInfo(@RequestBody UpdateUserInfoDTO dto) {
        dto.validParams();
        WebUserVO webUserVO = webUserService.updateUserInfo(dto, ContextHolder.getUserId());
        return GenericMsg.success(webUserVO);
    }

    @ApiOperation("用户修改用户头像")
    @PutMapping("/icon")
    public GenericMsg updateUserInfo(String icon) {
        webUserService.updateIcon(icon);
        return GenericMsg.success();
    }


    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public GenericMsg logout() {
        webLoginService.logout(ContextHolder.getUserId());
        return GenericMsg.success();
    }

}
