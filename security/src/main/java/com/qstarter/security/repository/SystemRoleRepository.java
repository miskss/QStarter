package com.qstarter.security.repository;

import com.qstarter.security.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @author peter
 * create: 2019-09-04 16:45
 **/
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {

    SystemRole findByRoleName(String roleName);

    @Query(value = "DELETE FROM system_user_role WHERE role_id = :roleId", nativeQuery = true)
    @Modifying
    void deleteUserRoleRelationshipByRoleId(Long roleId);

    @Query(value = "SELECT count(*) FROM system_user_role WHERE role_id = :roleId", nativeQuery = true)
    Integer countUserHasRole(Long roleId);

    Set<SystemRole> findByRoleNameIn(Set<String> roleName);

}



