package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemHtml;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author peter
 * date: 2019-11-07 14:45
 **/
@Data
@ApiModel
public class SystemHtmlItemVO {

    private Integer htmlId;

    private String htmlName;

    private String htmlType;

    private String htmlAddr;

    private Integer parentId;

    private String iconUrl;

    private String alias;

    private Integer sortNum;

    public static SystemHtmlItemVO fromEntity(SystemHtml html) {
        if (html == null) return null;
        SystemHtmlItemVO systemHtmlItemVO = new SystemHtmlItemVO();
        systemHtmlItemVO.setHtmlId(html.getId());
        systemHtmlItemVO.setHtmlName(html.getHtmlName());
        systemHtmlItemVO.setHtmlType(html.getHtmlType().getVal());
        systemHtmlItemVO.setHtmlAddr(html.getHtmlAddr());
        systemHtmlItemVO.setParentId(html.getParent() == null ? 0 : html.getParent().getId());
        systemHtmlItemVO.setIconUrl(html.getIconUrl());
        systemHtmlItemVO.setAlias(html.getAlias());
        systemHtmlItemVO.setSortNum(html.getSortNum());
        return systemHtmlItemVO;
    }
}
