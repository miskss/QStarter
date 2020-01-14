package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.core.model.AccessToken;
import lombok.Data;

import java.util.List;

/**
 * @author peter
 * date: 2019-09-06 11:30
 **/
@Data
public class WebLoginVO {
    private AccessToken token;

    private SystemRole role;

    private List<SystemResource> resources;

}
