package com.qstarter.security.repository;

import com.qstarter.security.entity.SystemHtml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author peter
 * date: 2019-11-07 11:00
 **/
public interface SystemHtmlRepository extends JpaRepository<SystemHtml, Integer> {

    List<SystemHtml> findByParentIsNull();

    List<SystemHtml> findByParent_Id(Integer parentId);

    List<SystemHtml> findByIdIn(Collection<Integer> ids);

    @Query(value = "DELETE from sys_role_html where sys_role_id =:roleId", nativeQuery = true)
    @Modifying
    void deleteSysHtmlRoleRelationshipByRoleId(Long roleId);

    @Query(value = "select count(*) from sys_role_html where sys_html_id=:sysHtmlId", nativeQuery = true)
    Long countByRoleBySystemHtmlId(Integer sysHtmlId);

    void deleteAllByParent_Id(Integer parentId);
}
