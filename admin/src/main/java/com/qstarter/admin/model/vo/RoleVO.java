package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author peter
 * date: 2019-09-09 16:07
 **/
@Data
@ApiModel
public class RoleVO {


    private Long roleId;

    private String roleName;

    private String roleDescription;


    public static RoleVO fromEntity(SystemRole systemRole) {
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleId(systemRole.getId());
        roleVO.setRoleName(systemRole.getRoleName());
        roleVO.setRoleDescription(systemRole.getRoleDescription());
        return roleVO;
    }
}
