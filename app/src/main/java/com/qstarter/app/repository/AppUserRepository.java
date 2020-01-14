package com.qstarter.app.repository;

import com.qstarter.app.entity.AppUser;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author peter
 * create: 2019-09-27 09:46
 **/
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    default AppUser findOne(Long id) {
        return findById(id).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.APP_USER_NOT_EXIST));
    }

    AppUser findByPhoneNumber(String phoneNumber);

    AppUser findByOpenId(String openId);




}
