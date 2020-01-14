package com.qstarter.admin.repository;

import com.qstarter.admin.entity.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author peter
 * create: 2019-09-06 10:18
 **/
public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    WebUser findByUsername(String username);

    Page<WebUser> findByUsernameNot(String superAdminUsername, Pageable pageable);

    WebUser findByPhoneNumber(String phoneNumber);

}
