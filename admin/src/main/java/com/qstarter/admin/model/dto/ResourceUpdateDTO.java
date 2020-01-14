package com.qstarter.admin.model.dto;

import com.qstarter.security.entity.SystemResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author peter
 * date: 2019-09-09 10:52
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ResourceUpdateDTO extends ResourceDTO {

    @NotNull(message = "资源id不能为空")
    @ApiModelProperty
    private Long resourceId;

    @Override
    public SystemResource convertToEntity() {
        SystemResource resources = super.convertToEntity();

        resources.setId(resourceId);

        return resources;
    }
}
