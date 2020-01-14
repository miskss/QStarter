package com.qstarter.security.repository;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.security.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author peter
 * create: 2019-09-04 16:44
 **/
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    SystemUser findByUsername(String username);

    default SystemUser findOne(Long id) {
        return this.findById(id).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "用户不存在"));
    }

    List<SystemUser> findByIdIn(Collection<Long> ids);

}
