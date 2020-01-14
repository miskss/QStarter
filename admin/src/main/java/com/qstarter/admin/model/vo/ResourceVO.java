package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemResource;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author peter
 * date: 2019-09-09 14:52
 **/
@Data
@ApiModel
public class ResourceVO {

    private Long id;

    private String resourceName;

    private String resourceUrl;

    private String operation;


    public static ResourceVO fromEntity(SystemResource resource) {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setId(resource.getId());
        resourceVO.setResourceName(resource.getResourceName());
        resourceVO.setResourceUrl(resource.getResourceUrl());
        resourceVO.setOperation(resource.getOperation().name());
        return resourceVO;
    }
}
