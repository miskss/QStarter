package com.qstarter.admin.model.vo;

import com.qstarter.security.entity.SystemHtml;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 页面树
 *
 * @author peter
 * date: 2019-11-07 16:14
 **/
@Data
public class HtmlTree implements Serializable {
    private static final long serialVersionUID = 3833115943829725475L;

    private Integer htmlId;

    private String htmlName;
    /**
     * 页面类型
     */
    private String htmlType;
    /**
     * 页面地址
     */
    private String htmlAddr;

    private Integer sortNum;
    private String iconUrl;

    private String alias;

    private List<HtmlTree> children;

    public static HtmlTree fromEntity(SystemHtml systemHtml) {
        HtmlTree htmlTree = new HtmlTree();
        htmlTree.setHtmlId(systemHtml.getId());
        htmlTree.setHtmlName(systemHtml.getHtmlName());
        htmlTree.setHtmlType(String.valueOf(systemHtml.getHtmlType()));
        htmlTree.setHtmlAddr(systemHtml.getHtmlAddr());
        htmlTree.setSortNum(systemHtml.getSortNum());
        htmlTree.setAlias(systemHtml.getAlias());
        htmlTree.setIconUrl(systemHtml.getIconUrl());
        return htmlTree;
    }
}
