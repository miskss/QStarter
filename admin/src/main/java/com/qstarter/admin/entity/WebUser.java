package com.qstarter.admin.entity;

import com.qstarter.core.entity.BaseEntity;
import com.qstarter.security.common.SystemUserNameEncoder;
import com.qstarter.security.common.SystemUserNameSuffix;
import com.qstarter.security.entity.SystemUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 网页端用户的管理
 * 只保存额外的信息，不保存密码信息，密码信息在{@link SystemUser}中
 *
 * @author peter
 * date: 2019-09-06 10:09
 **/
@Entity
@Table(name = "web_user", uniqueConstraints = @UniqueConstraint(name = "web_user_username_uni_idx", columnNames = "username"))
@Getter
@Setter
public class WebUser extends BaseEntity implements SystemUserNameEncoder {

    @Id
    private Long id;

    private String username;

    private String phoneNumber;

    private String iconUrl;

    @Override
    public String encodeUsername() {
        return this.username + SystemUserNameSuffix.WEB.getDescr();
    }

    public void initId(SystemUser systemUser) {
        initId(systemUser.getId());
    }

    public void initId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WebUser)) return false;

        WebUser webUser = (WebUser) o;

        return getId() != null ? getId().equals(webUser.getId()) : webUser.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
