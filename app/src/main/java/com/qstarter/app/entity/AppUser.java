package com.qstarter.app.entity;

import com.google.common.base.Strings;
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
import java.util.UUID;

/**
 * @author peter
 * date: 2019-09-27 09:21
 **/
@Entity
@Getter
@Setter
public class AppUser extends BaseEntity implements SystemUserNameEncoder {

    private static final int MAX_BANK_CARD = 3;
    @Id
    private Long id;

    private String phoneNumber;

    private String nickname;

    private String iconUrl;

    private String openId;


    private String unionId;


    public AppUser() {
    }


    public void setWXInfo(String nickname, String iconUrl, String openId, String unionId) {
        if (Strings.isNullOrEmpty(this.nickname)) {
            this.nickname = nickname;
        }

        if (Strings.isNullOrEmpty(iconUrl)) {
            this.iconUrl = iconUrl;
        }
        this.openId = openId;

        this.unionId = unionId;
    }

    public void setSystemUserId(SystemUser systemUser) {
        setId(systemUser.getId());
    }

    public String getNickname() {
        if (Strings.isNullOrEmpty(this.nickname)) {
            this.nickname = UUID.randomUUID().toString().substring(0, 5);
        }
        return this.nickname;
    }

    public AppUser(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String encodeUsername() {
        return Strings.isNullOrEmpty(this.phoneNumber) ? this.openId + SystemUserNameSuffix.APP.getDescr() : this.phoneNumber + SystemUserNameSuffix.APP.getDescr();
    }

    private void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;

        AppUser appUser = (AppUser) o;

        return getId() != null ? getId().equals(appUser.getId()) : appUser.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
