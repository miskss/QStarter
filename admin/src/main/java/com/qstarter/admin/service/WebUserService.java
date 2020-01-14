package com.qstarter.admin.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.qstarter.admin.entity.WebUser;
import com.qstarter.admin.model.dto.AddUserDTO;
import com.qstarter.admin.model.dto.UpdateUserDTO;
import com.qstarter.admin.model.dto.UpdateUserInfoDTO;
import com.qstarter.admin.model.vo.WebUserListItemVO;
import com.qstarter.admin.model.vo.WebUserVO;
import com.qstarter.admin.repository.WebUserRepository;
import com.qstarter.core.constant.CacheName;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.event.PublishEventCenter;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.thirdly_service.sms.SmsService;
import com.qstarter.core.utils.PageView;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.entity.SystemUser;
import com.qstarter.security.service.AccountService;
import com.qstarter.security.service.TokenManageService;
import com.qstarter.security.utils.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-09 17:33
 **/
@Service
@Transactional
@Slf4j
@CacheConfig(cacheNames = CacheName.WEB_USER)
public class WebUserService {

    //超级管理员的账户
    private static final String ROOT = "adminsuperme";
    private WebUserRepository repository;
    private AccountService accountService;
    private RoleService roleService;
    private TokenManageService tokenManageService;

    private SmsService memberSmsService;

    public WebUserService(WebUserRepository repository, AccountService accountService, RoleService roleService, TokenManageService tokenManageService, SmsService memberSmsService) {
        this.repository = repository;
        this.accountService = accountService;
        this.roleService = roleService;
        this.tokenManageService = tokenManageService;
        this.memberSmsService = memberSmsService;
    }

    /**
     * 查找用户
     *
     * @param userId 用户id
     * @return {@link WebUser}
     * @throws SystemServiceException 当用户不存在时
     */
    @Cacheable(key = "#userId")
    public WebUser findOne(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "用户不存在"));
    }

    /**
     * 分页查询
     *
     * @param pageIndex 开始页
     * @param pageSize  总页数
     * @return {@link PageView<WebUserListItemVO>}
     */
    public PageView<WebUserListItemVO> list(int pageIndex, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<WebUser> webUserPage = repository.findByUsernameNot(ROOT, pageRequest);
        Set<Long> collect = webUserPage.get().map(WebUser::getId).collect(Collectors.toSet());

        Map<Long, Set<SystemRole>> userIds = accountService.findRolesInUserIds(collect);

        return PageView.fromPage(webUserPage, webUser -> {
            Set<SystemRole> systemRoles = userIds.get(webUser.getId());
            return WebUserListItemVO.fromEntity(webUser, systemRoles);
        });
    }


    /**
     * 根据用户名来查找 网页后台的用户
     *
     * @param username 用户名
     * @return {@link WebUser}
     * @throws SystemServiceException 当用户不存在时，抛出异常
     */
    public WebUser findByUserName(String username) {
        WebUser byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            throw new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "用户不存在");
        }
        return byUsername;
    }

    public WebUser findByPhoneNumber(String phoneNumber) {
        WebUser byPhoneNumber = repository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber == null) {
            throw new SystemServiceException(ErrorMessageEnum.NO_DATA_EXIST_EXCEPTION, "手机号不存在");
        }
        return byPhoneNumber;
    }


    public WebUserListItemVO addWebUser(AddUserDTO dto) {
        WebUser byUsername = repository.findByUsername(dto.getUsername());

        if (byUsername != null) {
            throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "用户名已存在");
        }

        String phoneNumber = dto.getPhoneNumber();
        WebUser number = repository.findByPhoneNumber(phoneNumber);

        if (number != null) {
            throw new SystemServiceException(ErrorMessageEnum.PHONE_NUMBER_ALREADY_EXIST);
        }

        WebUser webUser = dto.convertToEntity();

        List<Long> roleIds = dto.getRoleIds();
        Set<SystemRole> roles = roleIds.stream().map(this::initRoles).flatMap(Collection::stream).collect(Collectors.toSet());

        //保存到系统用户中
        SystemUser systemUser = accountService.addSystemUser(webUser, dto.getPassword(), roles);

        webUser.initId(systemUser);

        webUser = repository.save(webUser);

        return WebUserListItemVO.fromEntity(webUser, accountService.findRolesByUserId(webUser.getId()));
    }

    /**
     * 根据userId 查找该用户 的角色
     *
     * @param userId 用户id
     * @return {@link Set<SystemRole>}
     */
    public Set<SystemRole> findRolesByUserId(Long userId) {
        return accountService.findRolesByUserId(userId);

    }

    @CachePut(key = "#result.id")
    public WebUser updateIcon(String icon) {
        WebUser user = findOne(ContextHolder.getUserId());

        if (Strings.isNullOrEmpty(icon)) return user;

        //如果头像的地址与之前不符，则先删除之前的图片在
        if (!Objects.equals(icon, user.getIconUrl())) {
            PublishEventCenter.publishDeleteFileEvent(user.getIconUrl());
            user.setIconUrl(icon);
            return user;
        }
        return user;
    }


    /**
     * 更新用户信息
     *
     * @param dto    {@link UpdateUserInfoDTO}
     * @param userId 用户id
     * @return {@link WebUserVO}
     */
    @CacheEvict(key = "#userId")
    public WebUserVO updateUserInfo(UpdateUserInfoDTO dto, Long userId) {

        //验证新输入的username 是否和库中冲突
        WebUser user = findOne(userId);

        String phoneNumber = dto.getPhoneNumber();
        if (StringUtils.hasText(phoneNumber)) {
            //验证手机号
            boolean match = memberSmsService.isMatch(phoneNumber, dto.getSmsCode());

            if (!match) {
                throw new SystemServiceException(ErrorMessageEnum.SMS_CODE_IS_NOT_MATCH);
            }
            if (!Objects.equals(user.getPhoneNumber(), phoneNumber)) {
                WebUser byPhoneNumber = repository.findByPhoneNumber(phoneNumber);
                if (byPhoneNumber != null) {
                    throw new SystemServiceException(ErrorMessageEnum.PHONE_NUMBER_ALREADY_EXIST);
                }
                user.setPhoneNumber(phoneNumber);
            }
        }

        String iconUrl = dto.getIconUrl();
        if (StringUtils.hasText(iconUrl)) {
            //如果头像的地址与之前不符，则先删除之前的图片在
            if (!Objects.equals(iconUrl, user.getIconUrl())) {
                PublishEventCenter.publishDeleteFileEvent(user.getIconUrl());
                user.setIconUrl(iconUrl);
            }
        }

        Set<SystemRole> rolesByUserId = accountService.findRolesByUserId(userId);

        return WebUserVO.fromEntity(user, rolesByUserId);

    }

    @CacheEvict(key = "#dto.userId")
    public WebUserListItemVO adminUpdateWebUser(UpdateUserDTO dto) {
        boolean isRemoveToken = false;

        WebUser old = findOne(dto.getUserId());
        //验证新输入的username 是否和库中冲突
        if (!Objects.equals(old.getUsername(), dto.getUsername())) {
            //用户名称有变动
            isRemoveToken = true;
            //则查看新的用户名库中是否已存在
            WebUser byUserName = repository.findByUsername(dto.getUsername());
            if (byUserName != null)
                throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "该用户名已存在");
        }
        //验证新输入的手机号是否和库中冲突
        if (!Objects.equals(old.getPhoneNumber(), dto.getPhoneNumber())) {
            //则查看新的用户名库中是否已存在
            WebUser byUserName = repository.findByPhoneNumber(dto.getPhoneNumber());
            if (byUserName != null)
                throw new SystemServiceException(ErrorMessageEnum.PHONE_NUMBER_ALREADY_EXIST);
        }

        Set<SystemRole> systemRoles = null;
        if (Objects.nonNull(dto.getRoleIds())) {
            systemRoles = dto.getRoleIds().stream().map(this::initRoles).flatMap(Collection::stream).collect(Collectors.toSet());
        }
        WebUser webUser = dto.convertToEntity();
        //判断密码是否有改变， 角色是否有改变
        boolean passwordAndRolesUpdated = accountService.isWebUserPasswordAndRolesUpdated(dto.getPassword(), webUser, dto.getUserId(), systemRoles);
        if (passwordAndRolesUpdated) {
            isRemoveToken = true;
        }
        repository.save(webUser);
        if (isRemoveToken) {
            tokenManageService.removeToken(dto.getUserId());
        }

        return WebUserListItemVO.fromEntity(webUser, accountService.findRolesByUserId(webUser.getId()));

    }

    @CacheEvict(key = "#userId")
    public void deleteUser(Long userId) {
        WebUser one = findOne(userId);
        if (Objects.equals(one.getUsername(), ROOT)) {
            throw new SystemServiceException(ErrorMessageEnum.BAD_REQUEST, "禁止删除超级管理员");
        }
        repository.delete(one);
        accountService.deleteSystemUser(userId);
    }

    /**
     * 初始化 角色信息
     *
     * @param roleId 角色id
     * @return {@link Set<SystemRole> }
     */
    private Set<SystemRole> initRoles(Long roleId) {
        SystemRole one = roleService.findOne(roleId);
        //添加系统默认的WEB角色到该用户下
        SystemRole webRole = roleService.findWEBRole();
        return ImmutableSet.of(one, webRole);
    }

}

