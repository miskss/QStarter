package com.qstarter.security.granter;

import com.qstarter.security.model.UserDetail;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 在整个验证过滤链中 流转 验证
 * <p>
 * 参考 {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 * 这边不涉及到密码，所以把{@code credentials} 属性给删除
 *
 * @author peter
 * date: 2019-09-24 10:47
 **/
public class IdAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // ~ Instance fields
    // ================================================================================================
    /**
     *     开始放入的就是userId，后续要查找到用户后 放入的是{@link UserDetail}
     */
    private final Object principal;

    // ~ Constructors
    // ===================================================================================================

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public IdAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal
     * @param authorities
     */
    public IdAuthenticationToken(Object principal,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }

    // ~ Methods
    // ========================================================================================================

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();

    }
}
