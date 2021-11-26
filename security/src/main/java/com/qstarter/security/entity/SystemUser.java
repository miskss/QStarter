package com.qstarter.security.entity;

import com.qstarter.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 系统中所有用户账户集合，这边只存储账户和密码信息，其他的用户信息需存储在自己的用户信息表中，
 * 但是其他的用户的表中要存储 system_user 的id，或拿这个id做主键。
 * <p>
 * 其他的用户表在插入数据之前必须 先插入system_user 的信息在插入自己的信息
 *
 * @author peter
 * date: 2019-09-04 11:01
 **/
@Getter
@Setter
@Entity
@Table(name = "system_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_username_uni_idx", columnNames = "username")
        }
)
public class SystemUser extends BaseEntity {

    private static final long serialVersionUID = 6548075308664138358L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "system_user_role",
            joinColumns = @JoinColumn(name = "system_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<SystemRole> systemRoles;


    public SystemUser() {
    }

    public SystemUser(String username, String password, Set<SystemRole> systemRoles) {
        this.username = username;
        this.password = password;
        this.systemRoles = systemRoles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUser)) return false;

        SystemUser that = (SystemUser) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
