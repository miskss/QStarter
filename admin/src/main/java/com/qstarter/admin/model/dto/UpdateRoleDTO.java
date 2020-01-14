package com.qstarter.admin.model.dto;

import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author peter
 * date: 2019-09-10 14:32
 **/
@Data
@ApiModel
public class UpdateRoleDTO extends AddRoleDTO {

    @NotNull(message = "角色的id不能为空")
    private Long roleId;

    @Override
    public SystemRole convertToEntity() {
        SystemRole systemRole = super.convertToEntity();
        systemRole.setId(roleId);
        return systemRole;
    }
}
