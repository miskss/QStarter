package com.qstarter.admin.controller;

import com.qstarter.admin.model.dto.AddUserDTO;
import com.qstarter.admin.model.dto.UpdateUserDTO;
import com.qstarter.admin.model.vo.WebUserListItemVO;
import com.qstarter.admin.service.WebUserService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.utils.PageView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author peter
 * date: 2019-09-11 08:50
 **/
@Api(tags = "【后台网页】对后台人员的管理，增删改查")
@RestController
@RequestMapping("/api/web/manage-user")
@Validated
public class ManageUserController {
    private WebUserService webUserService;

    public ManageUserController(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @ApiOperation("分页查询系统用户的列表")
    @GetMapping
    @PreAuthorize("hasPermission('','')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "开始页，从0开始"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据条数，最多50条")
    })
    public GenericMsg<PageView<WebUserListItemVO>> get(@RequestParam(defaultValue = "0") Integer pageIndex,
                                                       @RequestParam(defaultValue = "10") @Size(min = 1, max = 50, message = "每页多只能有50条,最少1条") Integer pageSize) {
        PageView<WebUserListItemVO> list = webUserService.list(pageIndex, pageSize);
        return GenericMsg.success(list);
    }

    @ApiOperation("添加系统的用户")
    @PostMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<WebUserListItemVO> add(@RequestBody @Valid AddUserDTO dto) {
        return GenericMsg.success(webUserService.addWebUser(dto));
    }

    @ApiOperation("更新系统用户信息")
    @PutMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<WebUserListItemVO> update(@RequestBody @Valid UpdateUserDTO dto) {
        //验证密码的长度
        dto.validParams();
        return GenericMsg.success(webUserService.adminUpdateWebUser(dto));
    }

    @ApiOperation("删除系统中的用户")
    @DeleteMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<Long> delete(@RequestParam @NotNull(message = "用户的id不能为空") Long userId) {
        webUserService.deleteUser(userId);
        return GenericMsg.success(userId);
    }

}
