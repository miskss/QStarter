package com.qstarter.admin.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 是否拥有树
 *
 * @author peter
 * date: 2019-11-08 10:49
 **/
@Data
@ApiModel
public class HasHtmlTreeVO implements Serializable {

    private static final long serialVersionUID = 746763598611997320L;


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

    private List<HasHtmlTreeVO> children;

    private String iconUrl;
    private String alias;

    private boolean own;

    public static HasHtmlTreeVO fromHtmlTree(HtmlTree tree, boolean isOwn) {
        HasHtmlTreeVO hasHtmlTreeVO = new HasHtmlTreeVO();
        hasHtmlTreeVO.setOwn(isOwn);
        hasHtmlTreeVO.setHtmlId(tree.getHtmlId());
        hasHtmlTreeVO.setHtmlName(tree.getHtmlName());
        hasHtmlTreeVO.setHtmlType(tree.getHtmlType());
        hasHtmlTreeVO.setHtmlAddr(tree.getHtmlAddr());
        hasHtmlTreeVO.setSortNum(tree.getSortNum());
        hasHtmlTreeVO.setAlias(tree.getAlias());
        hasHtmlTreeVO.setIconUrl(tree.getIconUrl());
        return hasHtmlTreeVO;
    }

}
