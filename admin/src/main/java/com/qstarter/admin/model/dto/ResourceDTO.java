package com.qstarter.admin.model.dto;

import com.qstarter.core.utils.EnumUtils;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.enums.OperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author peter
 * date: 2019-09-09 09:35
 **/
@Data
@ApiModel
public class ResourceDTO {

    @ApiModelProperty(value = "资源名称")
    private String resourceName;


    @NotBlank(message = "资源地址不能为空")
    @ApiModelProperty(value = "资源的地址")
    private String resourceUrl;

    @ApiModelProperty(value = "资源的操作类型（GET,POST,PUT,DELETE）,页面资源可以不传", allowableValues = "GET,POST,PUT,DELETE")
    private String operation;

    public SystemResource convertToEntity() {

        OperationEnum operationEnum;
        //判断  页面类型的资源可以没有父级id，但是接口类型的资源必须要有父级id
        operationEnum = EnumUtils.convertToEnum(OperationEnum.class, operation);
        SystemResource systemResource = new SystemResource();
        systemResource.setResourceName(this.resourceName);
        systemResource.setResourceUrl(this.resourceUrl);
        systemResource.setOperation(operationEnum);
        return systemResource;
    }


}
