package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemHtml;
import com.qstarter.security.entity.SystemResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-11-07 15:00
 **/
@Data
@ApiModel
public class SysHtmlDetailVO {

    private Integer htmlId;

    private String htmlName;

    private String htmlType;

    private String htmlAddr;

    private String iconUrl;

    private String alias;

    private Integer parentId;

    @ApiModelProperty
    private List<ResourceVO> resources;

    public static SysHtmlDetailVO fromEntity(SystemHtml html) {
        SysHtmlDetailVO sysHtmlDetailVO = new SysHtmlDetailVO();
        sysHtmlDetailVO.setParentId(html.getParent() == null ? null : html.getParent().getId());
        List<SystemResource> resources = html.getResources();
        List<ResourceVO> collect = resources.stream().map(ResourceVO::fromEntity).collect(Collectors.toList());
        sysHtmlDetailVO.setResources(collect);
        sysHtmlDetailVO.setHtmlId(html.getId());
        sysHtmlDetailVO.setHtmlName(html.getHtmlName());
        sysHtmlDetailVO.setHtmlType(String.valueOf(html.getHtmlType()));
        sysHtmlDetailVO.setHtmlAddr(html.getHtmlAddr());
        sysHtmlDetailVO.setIconUrl(html.getIconUrl());
        sysHtmlDetailVO.setAlias(html.getAlias());
        return sysHtmlDetailVO;
    }

}
