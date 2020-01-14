package com.qstarter.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author peter
 * date: 2019-11-07 11:08
 **/
@Data
@ApiModel
public class SysHtmlDTO {
    @NotBlank
    private String htmlName;

    @NotBlank
    @ApiModelProperty(value = "页面类型：页面-PAGE,功能-FUNC")
    private String htmlType;

    private String htmlAddr;

    @ApiModelProperty(value = "用于排序")
    private int sortNum;

    private Integer parentId;

    private String iconUrl;

    @ApiModelProperty
    private List<Long> resourceIds;

    private String alias;
}
