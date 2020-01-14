package com.qstarter.admin.model.dto;

import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author peter
 * date: 2019-09-10 08:52
 **/
@Data
@ApiModel
public class AddRoleDTO {

    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty("角色描述")
    @NotBlank(message = "角色描述不能为空")
    private String roleDescription;

    @ApiModelProperty("页面id的集合")
    @NotEmpty(message = "角色必须拥有可操作的页面功能")
    private Set<Integer> htmlIds;

    public SystemRole convertToEntity() {
        SystemRole systemRole = new SystemRole();
        systemRole.setRoleName(this.roleName);
        systemRole.setRoleDescription(this.roleDescription);
        return systemRole;
    }
}
