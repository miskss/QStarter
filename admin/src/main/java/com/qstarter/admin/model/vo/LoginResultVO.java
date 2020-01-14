package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemRole;
import com.qstarter.core.model.AccessToken;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-09 10:38
 **/
@Data
@ApiModel
public class LoginResultVO {

    private AccessToken accessToken;

    private Set<String> roles;


    public LoginResultVO(AccessToken accessToken, Set<SystemRole> roles) {
        this.accessToken = accessToken;
        this.roles = roles.stream().map(SystemRole::getRoleName).collect(Collectors.toSet());
    }
}
