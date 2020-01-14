package com.qstarter.security.repository;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.enums.OperationEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author peter
 * create: 2019-09-04 16:44
 **/
public interface SystemResourcesRepository extends JpaRepository<SystemResource, Long> {

    default SystemResource findOne(Long resourceId) {
        return this.findById(resourceId).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "资源不存在"));
    }

    @Query(value = "DELETE from system_role_resource where resource_id = :resourceId", nativeQuery = true)
    @Modifying
    void deleteResourceRoleRelationship(Long resourceId);


    @Query(value = "DELETE from system_role_resource where system_role_id = :roleId", nativeQuery = true)
    @Modifying
    void deleteResourceRoleRelationshipByRoleId(Long roleId);


    SystemResource findByResourceNameAndOperation(String resourceName, OperationEnum operationEnum);


}
