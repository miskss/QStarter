package com.qstarter.security.service;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.repository.SystemHtmlRepository;
import com.qstarter.security.repository.SystemResourcesRepository;
import com.qstarter.security.repository.SystemRoleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-10 13:41
 **/
@Service
@Transactional
public class SystemRoleService {


    private final SystemRoleRepository roleRepository;
    private final SystemResourcesRepository resourcesRepository;
    private final SystemHtmlRepository systemHtmlRepository;

    public SystemRoleService(SystemRoleRepository roleRepository, SystemResourcesRepository resourcesRepository, SystemHtmlRepository systemHtmlRepository) {
        this.roleRepository = roleRepository;
        this.resourcesRepository = resourcesRepository;
        this.systemHtmlRepository = systemHtmlRepository;
    }

    public Set<SystemRole> findByRoleNameIn(Set<String> roleNames) {
        return roleRepository.findByRoleNameIn(roleNames);
    }

    @Transactional
    public SystemRole findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    /**
     * 查询网页后台录入的 角色
     * 不包含系统定义的角色
     *
     * @return {@link List <SystemRole>}
     */
    public List<SystemRole> findAllWithoutSystemRole() {
        return roleRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime")).stream()
                //剔除系统中固定的用户
                .filter(role -> !RoleInSystem.contains(role.getRoleName()))
                .collect(Collectors.toList());
    }

    public SystemRole addRole(SystemRole role) {
        return addOrUpdateRole(role);
    }

    public SystemRole updateRole(SystemRole role) {
        return addOrUpdateRole(role);
    }

    /**
     * 增加或更新role
     *
     * @param role {@link SystemRole}
     */
    private SystemRole addOrUpdateRole(SystemRole role) {
        SystemRole byRoleName = roleRepository.findByRoleName(role.getRoleName());

        if (Objects.nonNull(byRoleName)) {
            //角色名称在数据库中已经有了，则判断是更新还是新增
            if (Objects.isNull(role.getId())) {
                //新增
                throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "角色已存在");
            }
            //更新操作,角色名称和数据库中其他的名称冲突
            if (!Objects.equals(role.getId(), byRoleName.getId())) {
                throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "角色已存在");
            }
        }
        roleRepository.save(role);
        RolePool.addOrUpdateRole(role);
        return role;
    }

    public void deleteRole(Long roleId) {

        SystemRole systemRole = roleRepository.findById(roleId).orElse(null);
        if (systemRole == null) return;

        checkCanDelete(systemRole);
        //删除某个角色和页面之间的关联关系
        systemHtmlRepository.deleteSysHtmlRoleRelationshipByRoleId(roleId);

        //要删除某个角色和资源的关联关系
        resourcesRepository.deleteResourceRoleRelationshipByRoleId(roleId);

        //再删除角色和用户的关联关系
        roleRepository.deleteUserRoleRelationshipByRoleId(roleId);

        //删除该角色
        roleRepository.delete(systemRole);

        RolePool.deleteRole(systemRole);
    }

    /**
     * 如果是基础角色则不能删除
     * 如果有用户关联了这个角色，则不能删除该角色
     *
     * @param systemRole 角色实体
     */
    private void checkCanDelete(SystemRole systemRole) {

        String roleName = systemRole.getRoleName();

        boolean contains = RoleInSystem.contains(roleName);

        if (contains) {
            throw new SystemServiceException(ErrorMessageEnum.BAD_REQUEST, "系统中的基础角色不能被删除");
        }

        Integer integer = roleRepository.countUserHasRole(systemRole.getId());


        if (integer != null && integer > 0) {
            throw new SystemServiceException(ErrorMessageEnum.BAD_REQUEST, "有用户关联了该角色，不能被删除");
        }


    }

    public SystemRole findOne(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "角色不存在"));
    }

    /**
     * 获取该角色所拥有的资源id 集合
     *
     * @param role {@link SystemRole}
     * @return 资源id 的集合
     */
    public Set<Long> getResourcesByRole(SystemRole role) {
        return RolePool.getResourcesByRoleName(role.getRoleName());
    }

}
