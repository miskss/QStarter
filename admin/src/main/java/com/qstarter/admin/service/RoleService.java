package com.qstarter.admin.service;

import com.qstarter.admin.model.dto.AddRoleDTO;
import com.qstarter.admin.model.vo.RoleDetailVO;
import com.qstarter.admin.model.vo.RoleVO;
import com.qstarter.admin.model.vo.RoleZTreePageVO;
import com.qstarter.admin.model.vo.ZTreePageVO;
import com.qstarter.security.entity.SystemHtml;
import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.service.SystemRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-09 16:09
 **/
@Service
@Transactional
public class RoleService {

    private SystemRoleService systemRoleService;
    private SystemHtmlService htmlService;

    public RoleService(SystemRoleService systemRoleService, SystemHtmlService htmlService) {
        this.systemRoleService = systemRoleService;
        this.htmlService = htmlService;
    }

    SystemRole findWEBRole() {
        return systemRoleService.findByRoleName(RoleInSystem.WEB.name());
    }


    public SystemRole findOne(Long roleId) {
        return systemRoleService.findOne(roleId);
    }


    public Set<SystemRole> findByRoleNames(Set<String> roleNames){
        return systemRoleService.findByRoleNameIn(roleNames);
    }


    /**
     * 查询网页后台录入的 角色
     * 不包含系统定义的角色
     *
     * @return {@link List<RoleVO>}
     */
    public List<RoleVO> findAllWithoutSystemRole() {

        List<SystemRole> systemRoles = systemRoleService.findAllWithoutSystemRole();
        return systemRoles.stream()
                //剔除系统中固定的用户
                .map(RoleVO::fromEntity)
                .collect(Collectors.toList());
    }


    public RoleVO addOrUpdateRole(AddRoleDTO dto) {
        Set<Integer> htmlIds = dto.getHtmlIds();
        List<SystemHtml> byIdIn = htmlService.findByIdIn(htmlIds);
        //收集资源
        Set<SystemResource> collect = byIdIn.stream().map(SystemHtml::getResources)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        SystemRole systemRole = dto.convertToEntity();
        systemRole.setResources(collect);
        systemRole.setHtmlSet(new HashSet<>(byIdIn));
        return addOrUpdateRole(systemRole);
    }

    public RoleDetailVO findOneDetail(Long roleId) {
        SystemRole one = systemRoleService.findOne(roleId);
        Set<SystemHtml> htmlSet = one.getHtmlSet();
        List<Integer> collect = htmlSet == null ? new ArrayList<>() :
                htmlSet.stream()
                        .map(SystemHtml::getId)
                        .collect(Collectors.toList());
        return RoleDetailVO.fromEntity(one, collect);
    }


    public List<RoleZTreePageVO> findOneZtree(Long roleId) {
        SystemRole one = systemRoleService.findOne(roleId);
        Set<Integer> roleHtmlIds = one.getHtmlSet().stream().map(SystemHtml::getId).collect(Collectors.toSet());
        List<ZTreePageVO> zTree = htmlService.findZTree();
        return  zTree.stream()
                .map(v -> {
                    Integer id = v.getId();
                    boolean contains = roleHtmlIds.contains(id);
                    return new RoleZTreePageVO(v.getId(), v.getName(), v.getPId(),0, contains);
                }).collect(Collectors.toList());
    }


    private RoleVO addOrUpdateRole(SystemRole role) {
        return RoleVO.fromEntity(systemRoleService.addRole(role));
    }

    public RoleVO updateRole(SystemRole role) {
        return RoleVO.fromEntity(systemRoleService.updateRole(role));
    }

    public void deleteRole(Long roleId) {
        systemRoleService.deleteRole(roleId);
    }


}
