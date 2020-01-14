package com.qstarter.admin.service;

import com.qstarter.admin.model.vo.HasHtmlTreeVO;
import com.qstarter.admin.model.vo.HtmlTree;
import com.qstarter.security.entity.SystemHtml;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.enums.RoleInSystem;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-11-07 15:50
 **/
@Service
public class SystemHtmlResourceService {

    private SystemHtmlService systemHtmlService;

    public SystemHtmlResourceService(SystemHtmlService systemHtmlService) {
        this.systemHtmlService = systemHtmlService;
    }

    public List<HtmlTree> findHtmlByRoles(Set<SystemRole> roles) {
        if (isSuperAdmin(roles)) {
            return systemHtmlService.findHtmlTree();
        }

        Set<SystemHtml> roleSystemHtml = findRoleSystemHtml(roles);

        return roleSystemHtml.stream().map(HtmlTree::fromEntity).
                distinct()
                .sorted(Comparator.comparingInt(HtmlTree::getSortNum))
                .collect(Collectors.toList());

    }


    /**
     * 角色对应的 所拥有的页面树
     *
     * @param role 角色
     * @return {@link List<HasHtmlTreeVO>}
     */
    public List<HasHtmlTreeVO> checkRoleHtml(SystemRole role) {
        if (isSuperAdmin(role)) {
            return superAdminHtmlTree();
        }
        List<HasHtmlTreeVO> hasHtmlTreeVOS = checkRoleHtml(Collections.singleton(role));

        hasHtmlTreeVOS.forEach(this::checkHasChildNode);

        return hasHtmlTreeVOS;
    }

    /**
     * 所有角色对应的 所拥有的页面树
     *
     * @param roles 角色集合
     * @return {@link List<HasHtmlTreeVO>}
     */
    public List<HasHtmlTreeVO> checkRoleHtml(Set<SystemRole> roles) {
        if (isSuperAdmin(roles)) {
            return superAdminHtmlTree();
        }
        Set<SystemHtml> roleSystemHtml = findRoleSystemHtml(roles);
        List<HtmlTree> htmlTree = systemHtmlService.findHtmlTree();
        List<HasHtmlTreeVO> collect = htmlTree.stream().map(tree -> fromTree(roleSystemHtml, tree))
                .sorted(Comparator.comparingInt(HasHtmlTreeVO::getSortNum))
                .collect(Collectors.toList());
        collect.forEach(this::checkHasChildNode);
        return collect;
    }

    /**
     * 超级管理员的 页面资源树，默认全部拥有
     *
     * @return {@link List<HasHtmlTreeVO>} 默认在root 根节点树下
     */
    private List<HasHtmlTreeVO> superAdminHtmlTree() {
        List<HtmlTree> htmlTree = systemHtmlService.findHtmlTree();
        return htmlTree.stream()
                .map(this::fromTree)
                .collect(Collectors.toList());
    }

    /**
     * 是否是超级管理员角色
     *
     * @param systemRole 角色
     * @return boolean
     */
    private boolean isSuperAdmin(SystemRole systemRole) {
        String roleName = systemRole.getRoleName();
        return RoleInSystem.isSuperAdmin(roleName);
    }

    /**
     * 是否是超级管理员角色
     *
     * @param roles 角色集合
     * @return true 角色集合中 拥有超级管理员{@link RoleInSystem#SUPER_ADMIN}的角色
     */
    private boolean isSuperAdmin(Collection<SystemRole> roles) {
        return roles.stream()
                .anyMatch(this::isSuperAdmin);
    }

    private HasHtmlTreeVO fromTree(Set<SystemHtml> roleHtmls, HtmlTree tree) {

        HasHtmlTreeVO hasHtmlTreeVO = HasHtmlTreeVO.fromHtmlTree(tree, containHtml(roleHtmls, tree));

        List<HtmlTree> children = tree.getChildren();

        if (children == null || children.isEmpty()) {
            return hasHtmlTreeVO;
        }

        List<HasHtmlTreeVO> collect = children.stream().map(o -> fromTree(roleHtmls, o)).collect(Collectors.toList());

        hasHtmlTreeVO.setChildren(collect);

        return hasHtmlTreeVO;
    }

    /**
     * 默认设置拥有该节点及节点下所有子节点
     *
     * @param tree {@link HtmlTree}
     * @return {@link HasHtmlTreeVO}
     */
    private HasHtmlTreeVO fromTree(HtmlTree tree) {
        HasHtmlTreeVO hasHtmlTreeVO = HasHtmlTreeVO.fromHtmlTree(tree, true);
        List<HtmlTree> children = tree.getChildren();

        if (children == null || children.isEmpty()) {
            return hasHtmlTreeVO;
        }
        List<HasHtmlTreeVO> collect = children
                .stream()
                .map(this::fromTree)
                .collect(Collectors.toList());
        hasHtmlTreeVO.setChildren(collect);
        return hasHtmlTreeVO;

    }

    private boolean containHtml(Set<SystemHtml> roleSystemHtml, HtmlTree node) {
        return roleSystemHtml
                .stream()
                .anyMatch(systemHtml -> Objects.equals(systemHtml.getId(), node.getHtmlId()));
    }


    private Set<SystemHtml> findRoleSystemHtml(Set<SystemRole> roles) {
        return roles.stream().map(SystemRole::getHtmlSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * 如果拥有子节点，则默认拥有父节点(解决前端的树框架bug)
     *
     * @param treeVO {@link HasHtmlTreeVO}
     */
    private void checkHasChildNode(HasHtmlTreeVO treeVO) {
        List<HasHtmlTreeVO> children = treeVO.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        children.forEach(this::checkHasChildNode);
        boolean anyMatch = children.stream().anyMatch(HasHtmlTreeVO::isOwn);
        if (anyMatch) {
            treeVO.setOwn(true);
        }

    }
}
