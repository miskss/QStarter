package com.qstarter.admin.controller;

import com.qstarter.admin.model.dto.ResourceDTO;
import com.qstarter.admin.model.dto.ResourceUpdateDTO;
import com.qstarter.admin.model.vo.ResourceVO;
import com.qstarter.admin.service.resources.ResourceService;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author peter
 * date: 2019-09-06 15:59
 **/
@Api(tags = "【后台网页】资源 的增删改查")
@RestController
@RequestMapping("/api/web/resources")
@Validated
public class ResourcesController {

    private ResourceService resourceService;

    public ResourcesController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @ApiOperation("查询或检索所有资源")
    @GetMapping("/search")
    @ApiImplicitParam(name = "search", value = "搜索的名称，不传时返回所有资源")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<List<ResourceVO>> search(String search) {
        return GenericMsg.success(resourceService.search(search));
    }

    @ApiOperation("资源列表")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true)
    })
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<PageView<ResourceVO>> list(@Min(value = 0) @RequestParam Integer pageIndex,
                                                 @Min(value = 1) @RequestParam Integer pageSize) {
        PageView<ResourceVO> pageView = resourceService.list(pageIndex, pageSize);
        return GenericMsg.success(pageView);
    }

    /**
     * 以下必须是超级管理员才可以 对资源进行增删改查
     */
    @ApiOperation(value = "新增资源【需要超级管理员的权限】")
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg<ResourceVO> add(@Valid @RequestBody ResourceDTO dto) {
        return GenericMsg.success(resourceService.add(dto));
    }

    @ApiOperation(value = "更新资源【需要超级管理员的权限】")
    @PutMapping
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg update(@Valid @RequestBody ResourceUpdateDTO dto) {
        return GenericMsg.success(resourceService.update(dto));
    }

    @ApiOperation(value = "删除资源【需要超级管理员的权限】")
    @DeleteMapping
    @PreAuthorize("hasAuthority(T(com.qstarter.security.enums.RoleInSystem).SUPER_ADMIN.name())")
    public GenericMsg<Long> delete(@RequestParam @NotNull(message = "资源id不能为空") Long resourceId) {
        resourceService.delete(resourceId);
        return GenericMsg.success(resourceId);
    }

}
