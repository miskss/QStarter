package com.qstarter.security.service;

import com.qstarter.security.entity.SystemUser;
import com.qstarter.security.model.UserDetail;
import com.qstarter.security.repository.SystemUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peter
 * date: 2019-09-04 16:50
 **/
@Service
public class SystemUserDetailService implements UserDetailsService {

    private final SystemUserRepository repository;

    public SystemUserDetailService(SystemUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", username));
        }
        return getUserDetails(user);
    }

    public UserDetails getUserDetails(Long userId) {
        SystemUser user = repository.findById(userId).orElse(null);
        if (user == null) {
            throw new InvalidGrantException("user not found");
        }
        String password = StringUtils.isEmpty(user.getPassword())?"":user.getPassword();
        user.setPassword(password);
        return getUserDetails(user);
    }

    private UserDetails getUserDetails(SystemUser user) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getSystemRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new UserDetail(user, authorities);
    }

}
