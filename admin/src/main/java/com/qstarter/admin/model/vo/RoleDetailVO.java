package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author peter
 * date: 2019-11-11 10:42
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class RoleDetailVO extends RoleVO {

    private List<Integer> htmlIds;


    public  static  RoleDetailVO fromEntity(SystemRole systemRole,List<Integer> htmlIds){
        RoleDetailVO roleDetailVO = new RoleDetailVO();
        roleDetailVO.setHtmlIds(htmlIds);
        roleDetailVO.setRoleId(systemRole.getId());
        roleDetailVO.setRoleName(systemRole.getRoleName());
        roleDetailVO.setRoleDescription(systemRole.getRoleDescription());
        return roleDetailVO;
    }
}
