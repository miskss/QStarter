package com.qstarter.security.entity;

import com.google.common.base.Objects;
import com.qstarter.core.entity.BaseEntity;
import com.qstarter.security.enums.OperationEnum;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

/**
 * 系统资源表
 * RoleInSystem n...n SystemResource
 *
 * @author peter
 * date: 2019-09-04 16:00
 **/
@Getter
@Setter
@Entity
public class SystemResource extends BaseEntity {

    private static final long serialVersionUID = 4133268899490752978L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName;

    private String resourceUrl;

    @Enumerated(value = EnumType.STRING)
    private OperationEnum operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemResource)) return false;
        SystemResource that = (SystemResource) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
