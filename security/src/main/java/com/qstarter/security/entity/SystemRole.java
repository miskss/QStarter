package com.qstarter.security.entity;

import com.qstarter.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * 角色 表
 *
 * @author peter
 * date: 2019-09-04 11:02
 **/
@Getter
@Setter
@Entity
@Table(name = "system_role")
public class SystemRole extends BaseEntity {

    private static final long serialVersionUID = 1140496588587534332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    /**
     * 页面
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_html",
            joinColumns = @JoinColumn(name = "sys_role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sys_html_id", referencedColumnName = "id"))
    private Set<SystemHtml> htmlSet;

    @ManyToMany
    @JoinTable(name = "system_role_resource",
            joinColumns = @JoinColumn(name = "system_role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id")
    )
    private Set<SystemResource> resources;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemRole)) return false;

        SystemRole that = (SystemRole) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
