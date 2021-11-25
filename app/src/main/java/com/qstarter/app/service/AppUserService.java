package com.qstarter.app.service;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.qstarter.app.entity.AppUser;
import com.qstarter.app.event.AppUserEventCenter;
import com.qstarter.app.model.vo.*;
import com.qstarter.app.repository.AppUserRepository;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.model.AccessToken;
import com.qstarter.core.thirdly_service.weixin.WxLoginService;
import com.qstarter.core.thirdly_service.weixin.WxUserInfo;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.entity.SystemUser;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.service.AccountService;
import com.qstarter.security.service.SystemRoleService;
import com.qstarter.security.utils.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * app user 注册、登录、重置密码、修改密码、设置密码、登出
 *
 * @author peter
 * date: 2019-09-27 10:21
 **/
@Service
@Slf4j
public class AppUserService {


    private final AppUserRepository repository;
    private final SystemRoleService systemRoleService;
    private final AccountService accountService;
    private final WxLoginService wxLoginService;


    public AppUserService(AppUserRepository repository, SystemRoleService systemRoleService, AccountService accountService, WxLoginService wxLoginService) {
        this.repository = repository;
        this.systemRoleService = systemRoleService;
        this.accountService = accountService;
        this.wxLoginService = wxLoginService;
    }


    @Transactional
    public AppUserVO getAppUserInfo() {

        AppUser appUser = findOne();


        return AppUserVO.fromEntity(appUser);
    }


    /**
     * 手机号密码注册
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    @Transactional
    public void registerAppUser(String phoneNumber, String password) {
        AppUser byPhoneNumber = repository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber != null) {
            throw new SystemServiceException(ErrorMessageEnum.APP_REPEAT_REGISTERED);
        }
        AppUser appUser = register(phoneNumber, password);
        repository.save(appUser);
    }


    /**
     * 手机号 密码登录
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @return {@link AccessToken}
     */
    public AccessToken login(String phoneNumber, String password) {
        AppUser user = findByPhoneNumber(phoneNumber);
        OAuth2AccessToken token = accountService.getToken(user.encodeUsername(), password);
        return AccessToken.convert(token);
    }

    /**
     * 手机号 短信登录 注册
     *
     * @param phoneNumber 手机号
     * @return {@link AccessToken}
     */
    public AccessToken login(String phoneNumber) {
        AppUser user = getAppUserElseRegister(phoneNumber);
        //获取 用户id
        return login(user);
    }

    public AccessToken login(AppUser user) {
        OAuth2AccessToken token = accountService.getToken(user.getId());
        return AccessToken.convert(token);
    }

    /**
     * 设置密码
     *
     * @param userId   用户id
     * @param password 密码
     */
    @Transactional
    public void setPassword(Long userId, String password) {
        repository.findOne(userId);
        accountService.setPassword(userId, password);
    }

    /**
     * 修改密码
     *
     * @param userId      用户密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        repository.findOne(userId);
        accountService.changeUserPassword(userId, oldPassword, newPassword);
    }

    @Transactional
    public void resetPassword(String phoneNumber, String newPassword, AfterCheckUserExist checkUserExist) {
        AppUser one = repository.findByPhoneNumber(phoneNumber);
        if (one == null) {
            throw new SystemServiceException(ErrorMessageEnum.APP_USER_NOT_EXIST);
        }
        checkUserExist.callBack();
        accountService.resetPassword(one.getId(), newPassword);
    }

    /**
     * 绑定或更换手机号
     *
     * @param userId         用户id
     * @param newPhoneNumber 新的手机号
     */
    @Transactional
    public void bindOrChangePhoneNumber(Long userId, String newPhoneNumber) {
        AppUser one = repository.findOne(userId);

        if (Objects.equals(newPhoneNumber, one.getPhoneNumber())) return;

        AppUser byPhoneNumber = repository.findByPhoneNumber(newPhoneNumber);

        if (byPhoneNumber != null) {
            throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "该手机号已被绑定");
        }
        boolean flag = Strings.isNullOrEmpty(one.getPhoneNumber());
        one.setPhoneNumber(newPhoneNumber);
        accountService.bindOrUpdateAppUsername(one.getId(), one);
        if (flag) {
            AppUserEventCenter.publishAppUserBindPhoneNumberEvent(one.getId());
        }
    }

    /**
     * 用户绑定微信
     *
     * @param code code 值
     */
    @Transactional
    public void bindWX(String code) {
        AppUser one = findOne();
        WxUserInfo wxUserInfo = wxLoginService.getWxUserInfo(code);
        String openid = wxUserInfo.getOpenid();
        if (Objects.equals(openid, one.getOpenId())) {
            return;
        }
        AppUser appUser = repository.findByOpenId(openid);
        if (appUser != null) {
            throw new SystemServiceException(ErrorMessageEnum.DATA_ALREADY_EXIST_EXCEPTION, "该微信已被绑定");
        }
        boolean flag = Strings.isNullOrEmpty(one.getOpenId());
        if (flag) {
            AppUserEventCenter.publishAppUserBindWXEvent(one.getId());
        }
        one.setWXInfo(wxUserInfo.getNickname(), wxUserInfo.getHeadimgurl(), openid, wxUserInfo.getUnionid());
        repository.save(one);
    }


    /**
     * 登出
     *
     * @param userId 用户id
     */
    public void logout(Long userId) {
        accountService.logout(userId);
    }






    /**
     * 用户列表
     *
     * @param pageIndex   页序号
     * @param pageSize    页大小
     * @param phoneNumber 手机号
     * @return {@link Page<AppUser>}
     */
    public Page<AppUser> query(Integer pageIndex, Integer pageSize, String phoneNumber) {
        Specification<AppUser> specification = (Specification<AppUser>) (root, query, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            if (!Strings.isNullOrEmpty(phoneNumber)) {
                Predicate predicate = criteriaBuilder.like(root.get("phoneNumber"), '%' + phoneNumber + '%');
                predicatesList.add(predicate);
            }
            return criteriaBuilder.and(predicatesList.toArray(new Predicate[0]));
        };

        Sort orderByCreateTime = Sort.by(Sort.Direction.DESC, "createTime");

        return repository.findAll(specification, PageRequest.of(pageIndex, pageSize, orderByCreateTime));
    }


    /**
     * vip 用户列表
     *
     * @param pageIndex   页序号
     * @param pageSize    页大小
     * @param phoneNumber 手机号
     * @param nickname    昵称
     * @param colSortIndex 排序的列
     * @return Page<AppUser>
     */
    public Page<AppUser> query(Integer pageIndex, Integer pageSize, String phoneNumber, String nickname,Integer colSortIndex,String sort) {
        Specification<AppUser> specification = (Specification<AppUser>) (root, query, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            if (!Strings.isNullOrEmpty(phoneNumber)) {
                Predicate predicate = criteriaBuilder.like(root.get("phoneNumber"), '%' + phoneNumber + '%');
                predicatesList.add(predicate);
            }

            if (!Strings.isNullOrEmpty(nickname)) {
                Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("nickname"), "%" + nickname + "%"));
                predicatesList.add(predicate);
            }

            return criteriaBuilder.and(predicatesList.toArray(new Predicate[0]));
        };

        if (Objects.equals(colSortIndex,4)){
            Sort.Direction desc = Sort.Direction.DESC;
            if (Objects.equals("asc",sort)){
                desc = Sort.Direction.ASC;
            }
            Sort orderByGold =  Sort.by(desc, "goldAccount_currentGold");
            return  repository.findAll(specification,PageRequest.of(pageIndex, pageSize, orderByGold));

        }
        Sort orderByCreateTime = Sort.by(Sort.Direction.DESC, "createTime");

        return repository.findAll(specification, PageRequest.of(pageIndex, pageSize, orderByCreateTime));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AppUser getAppUserElseRegister(String phoneNumber) {
        AppUser user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            //新用户需要注册
            user = new AppUser(phoneNumber);
            //权限
            Set<SystemRole> systemRoles = initRoleList();
            SystemUser systemUser = accountService.addSystemUser(user, systemRoles);
            user.setSystemUserId(systemUser);
            user = repository.save(user);
            AppUserEventCenter.publishAppUserRegisterEvent(user.getId());
        }
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AppUser getAppUserElseRegisterByOpenId(String code) {
        WxUserInfo wxUserInfo = wxLoginService.getWxUserInfo(code);
        AppUser byOpenId = repository.findByOpenId(wxUserInfo.getOpenid());

        if (byOpenId == null) {
            //新用户需要注册
            byOpenId = new AppUser();
            byOpenId.setWXInfo(wxUserInfo.getNickname(), wxUserInfo.getHeadimgurl(), wxUserInfo.getOpenid(), wxUserInfo.getUnionid());
            //权限
            Set<SystemRole> systemRoles = initRoleList();
            SystemUser systemUser = accountService.addSystemUser(byOpenId, systemRoles);
            byOpenId.setSystemUserId(systemUser);
            byOpenId = repository.save(byOpenId);
            AppUserEventCenter.publishAppUserRegisterEvent(byOpenId.getId());
        }
        return byOpenId;
    }


    @Transactional
    public AppUser update(String nickname, String iconUrl) {
        AppUser one = findOne();
        if (!Strings.isNullOrEmpty(nickname)) {
            one.setNickname(nickname);
        }
        if (!Strings.isNullOrEmpty(iconUrl)) {
            one.setIconUrl(iconUrl);
        }
        return one;
    }


    @Transactional(readOnly = true)
    public AppUser findOne() {
        Long userId = ContextHolder.getUserId();
        return repository.findOne(userId);
    }

    @Transactional(readOnly = true)
    public AppUser findOne(Long appUserId) {
        return repository.findOne(appUserId);
    }

    private AppUser findByPhoneNumber(String phoneNumber) {
        AppUser byPhoneNumber = repository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber == null) {
            throw new SystemServiceException(ErrorMessageEnum.APP_USER_PHONE_OR_PASSWORD_ERROR);
        }
        return byPhoneNumber;
    }

    private Set<SystemRole> initRoleList() {
        SystemRole appRole = systemRoleService.findByRoleName(RoleInSystem.APP.name());
        return Sets.newHashSet(appRole);
    }


    private AppUser register(String phoneNumber, String password) {
        AppUser appUser = new AppUser(phoneNumber);
        //权限
        Set<SystemRole> systemRoles = initRoleList();
        SystemUser systemUser = accountService.addSystemUser(appUser, password, systemRoles);

        appUser.setSystemUserId(systemUser);
        return appUser;
    }

    @FunctionalInterface
    public interface AfterCheckUserExist {
        void callBack();
    }

}
