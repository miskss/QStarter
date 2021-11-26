package com.qstarter.security.entity;

import com.google.common.base.Objects;
import com.qstarter.core.entity.BaseEntity;
import com.qstarter.security.enums.HtmlType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面资源类管理
 *
 * @author peter
 * date: 2019-11-07 10:54
 **/
@Entity
@Setter
@Getter
public class SystemHtml extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8244592664081929287L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String htmlName;

    private String iconUrl;
    /**
     * 页面类型
     */
    @Enumerated(value = EnumType.STRING)
    private HtmlType htmlType;

    /**
     * 页面地址
     */
    private String htmlAddr;
    /**
     * 排序编号
     */
    private int sortNum;

    /**
     * 页面别名
     */
    private String alias;

    @ManyToMany
    private List<SystemResource> resources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private SystemHtml parent;

    public static SystemHtml of(String htmlName, HtmlType htmlType, int sortNum, String htmlAddr, String iconUrl,String alias) {
        SystemHtml html = new SystemHtml();
        html.setHtmlName(htmlName);
        html.setSortNum(sortNum);
        html.setHtmlType(htmlType);
        html.setHtmlAddr(htmlAddr);
        html.setIconUrl(iconUrl);
        html.setAlias(alias);
        return html;
    }

    public static SystemHtml of(String htmlName, HtmlType htmlType, String htmlAddr, int sortNum, String iconUrl,String alias, SystemHtml parent, List<SystemResource> resources) {
        SystemHtml html = of(htmlName, htmlType, sortNum, htmlAddr, iconUrl,alias);
        html.setParent(parent);
        if (resources != null) {
            ArrayList<SystemResource> systemResources = new ArrayList<>(resources);
            html.setResources(systemResources);
        }
        return html;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemHtml)) return false;
        SystemHtml that = (SystemHtml) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
