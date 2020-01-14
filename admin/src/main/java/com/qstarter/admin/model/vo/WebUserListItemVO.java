package com.qstarter.admin.model.vo;

import com.qstarter.admin.entity.WebUser;
import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-11 17:35
 **/
@Data
@ApiModel
public class WebUserListItemVO {

    private Long id;

    private String username;

    private String phoneNumber;

    private String iconUrl;

    private Set<RoleVO> roles;

    public static WebUserListItemVO fromEntity(WebUser webUser, Set<SystemRole> roles) {

        WebUserListItemVO webUserListItemVO = new WebUserListItemVO();
        webUserListItemVO.setId(webUser.getId());
        webUserListItemVO.setUsername(webUser.getUsername());
        webUserListItemVO.setPhoneNumber(webUser.getPhoneNumber());
        webUserListItemVO.setIconUrl(webUser.getIconUrl());

        Set<RoleVO> roleVOS = null;
        if (roles != null){
            roleVOS = roles.stream().map(RoleVO::fromEntity).collect(Collectors.toSet());
        }
        webUserListItemVO.setRoles(roleVOS);
        return webUserListItemVO;

    }
}
