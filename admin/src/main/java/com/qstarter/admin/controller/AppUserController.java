package com.qstarter.admin.controller;

import com.qstarter.app.entity.AppUser;
import com.qstarter.app.service.AppUserService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.utils.PageView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author peter
 * date: 2019-10-24 15:17
 **/
@Api(tags = "【后台网页】App 用户")
@RestController
@RequestMapping("/api/web/app-user")
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @ApiOperation("获取app用户列表")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页序号，从0 开始", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数，最小为1", required = true),
            @ApiImplicitParam(name = "phoneNumber", value = "待查询的手机号（支持模糊）")
    })
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<PageView<AppUser>> list(@RequestParam(defaultValue = "0") Integer pageIndex,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                String phoneNumber) {
        Page<AppUser> query = appUserService.query(pageIndex, pageSize, phoneNumber);
        return GenericMsg.success(PageView.fromPage(query, o -> o));
    }


}
