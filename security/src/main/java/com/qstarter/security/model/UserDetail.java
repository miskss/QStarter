package com.qstarter.security.model;

import com.qstarter.security.entity.SystemUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author peter
 * date: 2019-09-04 16:47
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UserDetail extends User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private Long systemUserId;

    public UserDetail(SystemUser user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.systemUserId = user.getId();
    }

    public UserDetail(SystemUser user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.systemUserId = user.getId();
    }
}
