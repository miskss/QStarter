package com.qstarter.security.service;

import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.repository.SystemRoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 缓存角色和资源的关联关系池
 *
 * @author peter
 * date: 2019-09-09 15:16
 **/
@Service
public class RolePool implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 角色下对应资源集合
     */
    private final static ConcurrentHashMap<String, Set<Long>> ROLE_POOL = new ConcurrentHashMap<>();

    private SystemRoleRepository roleRepository;

    public RolePool(SystemRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<SystemRole> roles = roleRepository.findAll().stream()
                //剔除系统中固定的用户
                .filter(role -> !RoleInSystem.contains(role.getRoleName()))
                .collect(Collectors.toList());
        Map<String, Set<Long>> collect = new HashMap<>();
        roles.forEach(systemRole -> {
            Set<Long> set = getRoleResourceIds(systemRole);
            if (set.isEmpty()) return;
            if (collect.put(systemRole.getRoleName(), set) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        });
        ROLE_POOL.putAll(collect);
    }


    public static Set<Long> getResourcesByRoleName(String roleName) {

        if (RoleInSystem.isSuperAdmin(roleName)) {
            return ResourcePool.getAllResourcesId();
        }

        return ROLE_POOL.get(roleName);
    }

    static void addOrUpdateRole(SystemRole role) {
        ROLE_POOL.put(role.getRoleName(), getRoleResourceIds(role));

    }

    static void addRole(SystemRole role) {
        ROLE_POOL.putIfAbsent(role.getRoleName(), getRoleResourceIds(role));
    }

    static void deleteRole(SystemRole role) {
        ROLE_POOL.remove(role.getRoleName());
    }


    static void updateRole(SystemRole role) {
        ROLE_POOL.replace(role.getRoleName(), getRoleResourceIds(role));
    }

    private static Set<Long> getRoleResourceIds(SystemRole role) {
        return role.getResources().stream().map(SystemResource::getId).collect(Collectors.toSet());
    }

}
