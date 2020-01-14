package com.qstarter.admin.controller;

import com.qstarter.admin.model.dto.ModifySysHtmlDTO;
import com.qstarter.admin.model.dto.SysHtmlDTO;
import com.qstarter.admin.model.vo.*;
import com.qstarter.admin.service.SystemHtmlResourceService;
import com.qstarter.admin.service.SystemHtmlService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.utils.PageView;
import com.qstarter.security.entity.SystemHtml;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.service.AccountService;
import com.qstarter.security.utils.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author peter
 * date: 2019-11-07 11:06
 **/
@RestController
@RequestMapping("/api/web/html")
@Validated
@Api(tags = "【后台接口】后台页面/功能")
public class SystemHtmlController {

    private AccountService accountService;
    private SystemHtmlService systemHtmlService;
    private SystemHtmlResourceService systemHtmlResourceService;

    public SystemHtmlController(AccountService accountService, SystemHtmlService systemHtmlService, SystemHtmlResourceService systemHtmlResourceService) {
        this.accountService = accountService;
        this.systemHtmlService = systemHtmlService;
        this.systemHtmlResourceService = systemHtmlResourceService;
    }

    @ApiOperation("页面资源树")
    @GetMapping("/tree")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<HtmlTree>> tree() {
        List<HtmlTree> htmlTree = systemHtmlService.findHtmlTree();
        return GenericMsg.success(htmlTree);
    }



    @ApiOperation("页面的ztree树")
    @GetMapping("/ztree")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<ZTreePageVO>> ztree() {
        return GenericMsg.success(systemHtmlService.findZTree());
    }



    @ApiOperation("获取某个用户的所拥有的页面资源树")
    @GetMapping("/user")
    public GenericMsg<List<HasHtmlTreeVO>> userRoleHtml() {
        Long userId = ContextHolder.getUserId();
        Set<SystemRole> roles = accountService.findRolesByUserId(userId);
        return GenericMsg.success(systemHtmlResourceService.checkRoleHtml(roles));
    }


    @ApiOperation("新增后台页面/功能【需要超级管理员的权限】")
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg add(@RequestBody @Valid SysHtmlDTO dto) {
        systemHtmlService.add(dto);
        return GenericMsg.success();
    }

    @ApiOperation("编辑后台页面/功能【需要超级管理员的权限】")
    @PutMapping
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg update(@RequestBody @Valid ModifySysHtmlDTO dto) {
        systemHtmlService.update(dto);
        return GenericMsg.success();
    }

    @ApiOperation("删除后台页面/功能【需要超级管理员的权限】")
    @DeleteMapping
    @ApiImplicitParam(name = "htmlId", value = "页面id")
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg delete(@NotNull @RequestParam Integer htmlId) {
        systemHtmlService.delete(htmlId);
        return GenericMsg.success();
    }

    @ApiOperation("获取后台页面/功能")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条")
    })
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<PageView<SystemHtmlItemVO>> list(@Min(value = 0) @RequestParam Integer pageIndex,
                                                       @Min(value = 1) @RequestParam Integer pageSize) {
        Page<SystemHtml> list = systemHtmlService.list(pageIndex, pageSize);
        PageView<SystemHtmlItemVO> pageView = PageView.fromPage(list, SystemHtmlItemVO::fromEntity);
        return GenericMsg.success(pageView);
    }

    @ApiOperation("页面功能详情")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "htmlId", value = "页面id")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<SysHtmlDetailVO> detail(@NotNull @RequestParam Integer htmlId) {
        return GenericMsg.success(systemHtmlService.detail(htmlId));
    }
}
