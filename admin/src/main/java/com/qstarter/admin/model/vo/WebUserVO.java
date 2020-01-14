package com.qstarter.admin.model.vo;

import com.qstarter.admin.entity.WebUser;
import com.qstarter.security.entity.SystemRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-09 17:35
 **/
@Data
@ApiModel
public class WebUserVO {

    private Long id;

    private String username;

    private String phoneNumber;

    private String iconUrl;

    private Set<String> roles;


    public static WebUserVO fromEntity(WebUser webUser,Set<SystemRole> roles){
        WebUserVO webUserVO = new WebUserVO();
        webUserVO.setId(webUser.getId());
        webUserVO.setUsername(webUser.getUsername());
        webUserVO.setPhoneNumber(webUser.getPhoneNumber());
        webUserVO.setIconUrl(webUser.getIconUrl());

        Set<String> collect = roles.stream().map(SystemRole::getRoleName).collect(Collectors.toSet());
        webUserVO.setRoles(collect);
        return webUserVO;
    }


}
