package com.qstarter.admin.controller;

import com.qstarter.admin.model.dto.AddRoleDTO;
import com.qstarter.admin.model.dto.UpdateRoleDTO;
import com.qstarter.admin.model.vo.HasHtmlTreeVO;
import com.qstarter.admin.model.vo.RoleDetailVO;
import com.qstarter.admin.model.vo.RoleVO;
import com.qstarter.admin.model.vo.RoleZTreePageVO;
import com.qstarter.admin.service.RoleService;
import com.qstarter.admin.service.SystemHtmlResourceService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author peter
 * date: 2019-09-09 16:05
 **/
@Api(tags = "【后台网页】角色 ")
@RestController
@RequestMapping("/api/web/roles")
public class RoleController {

    private RoleService roleService;
    private SystemHtmlResourceService systemHtmlResourceService;


    public RoleController(RoleService roleService, SystemHtmlResourceService systemHtmlResourceService) {
        this.roleService = roleService;
        this.systemHtmlResourceService = systemHtmlResourceService;
    }

    @GetMapping
    @ApiOperation("系统中角色的列表")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<RoleVO>> roles() {
        List<RoleVO> roleVOS = roleService.findAllWithoutSystemRole();
        return GenericMsg.success(roleVOS);
    }

    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<RoleVO> addRole(@RequestBody @Valid AddRoleDTO dto) {
        return GenericMsg.success(roleService.addOrUpdateRole(dto));
    }

    @ApiOperation("编辑角色")
    @PutMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<RoleVO> updateRole(@RequestBody @Valid UpdateRoleDTO dto) {
        return GenericMsg.success(roleService.addOrUpdateRole(dto));
    }

    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<Long> deleteRole(@RequestParam @NotNull(message = "角色id不能为空") Long roleId) {
        roleService.deleteRole(roleId);
        return GenericMsg.success(roleId);
    }

    @ApiOperation("获取某个角色的所拥有的页面资源树【需要拥有该资源时才能访问】")
    @GetMapping("/one")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true)
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<HasHtmlTreeVO>> htmlByRole(@NotNull @RequestParam Long roleId) {
        SystemRole role = roleService.findOne(roleId);
        return GenericMsg.success(systemHtmlResourceService.checkRoleHtml(role));
    }

    @ApiOperation("获取某个角色的明细【需要拥有该资源时才能访问】")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true)
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<RoleDetailVO> detail(@NotNull @RequestParam Long roleId) {
        return GenericMsg.success(roleService.findOneDetail(roleId));
    }

    @GetMapping("/ztree")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<RoleZTreePageVO>> ztree(@NotNull @RequestParam Long roleId) {
        return GenericMsg.success(roleService.findOneZtree(roleId));
    }

}
