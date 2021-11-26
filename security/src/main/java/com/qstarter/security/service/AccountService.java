package com.qstarter.security.service;

import com.google.common.base.Strings;
import com.qstarter.core.constant.CommonArgumentsValidConstant;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.HttpUtils;
import com.qstarter.core.utils.PasswordEncoderUtils;
import com.qstarter.security.common.SystemUserFactory;
import com.qstarter.security.common.SystemUserNameEncoder;
import com.qstarter.security.entity.SystemRole;
import com.qstarter.security.entity.SystemUser;
import com.qstarter.security.enums.RoleInSystem;
import com.qstarter.security.repository.SystemUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-05 17:33
 **/
@Service
@Slf4j
@Transactional
public class AccountService {
    /**
     * 短信登录的用户设置一个默认密码
     */
    public static final String DEFAULT_PASSWORD = "$2a$10$7wJxiIbLIJopv9K7ihMFc.OAo.qVw/xN0c5cwMUs/TejQHO39rWDm";

    static final String CLIENT = "walkmoneyjwtclient";
    private static final String SECRET = "f7f51e6ed2ca45f884d46bacf934ba8d";
    private static final String LOCALHOST = "http://127.0.0.1:";

    private static final String OAUTH_TOKEN = "/oauth/token";

    private static final String GRANT_TYPE = "grant_type";

    private static final String GRANT_PASSWORD = "password";
    private static final String GRANT_REFRESH_TOKEN = "refresh_token";
    private static final String GRANT_CUSTOM = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER_ID = "user_id";
    private static final String LOCAL_SERVER_PORT = "local.server.port";
    private static final String LOCAL_SERVER_CONTEXT_PATH = "server.servlet.context-path";

    private final SystemUserRepository repository;

    private final Environment environment;

    private final TokenManageService tokenManageService;


    public AccountService(SystemUserRepository repository, Environment environment, TokenManageService tokenManageService) {
        this.repository = repository;
        this.environment = environment;
        this.tokenManageService = tokenManageService;
    }

    /**
     * 新增用户
     *
     * @param encoder     {@link SystemUserNameEncoder}
     * @param rawPassword 未编码的密码
     * @param systemRoles {@link Set<SystemRole>}
     * @return {@link SystemUser}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public SystemUser addSystemUser(SystemUserNameEncoder encoder, String rawPassword, Set<SystemRole> systemRoles) {
        SystemUser systemUser = repository.findByUsername(encoder.encodeUsername());
        if (systemUser != null) return systemUser;
        systemUser = SystemUserFactory.createSystemUser(encoder, rawPassword, systemRoles);
        return repository.save(systemUser);
    }

    /**
     * 新增用户
     *
     * @param encoder     {@link SystemUserNameEncoder}
     * @param systemRoles {@link Set<SystemRole>}  角色
     * @return {@link SystemUser}
     */
    @Transactional
    public SystemUser addSystemUser(SystemUserNameEncoder encoder, Set<SystemRole> systemRoles) {
        return addSystemUser(encoder, DEFAULT_PASSWORD, systemRoles);
    }

    /**
     * 根据用户名和密码获取token
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link OAuth2AccessToken}
     */
    public OAuth2AccessToken getToken(String username, String password) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add(GRANT_TYPE, GRANT_PASSWORD);
        map.add(USERNAME, username);
        map.add(PASSWORD, password);

        return getOAuth2AccessToken(map);
    }

    /**
     * 根据用户id来获取 token
     *
     * @param userId 用户id
     * @return {@link OAuth2AccessToken}
     */
    public OAuth2AccessToken getToken(Long userId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add(GRANT_TYPE, GRANT_CUSTOM);
        map.add(USER_ID, String.valueOf(userId));
        return getOAuth2AccessToken(map);
    }

    /**
     * 根据refresh_token 来获取token
     *
     * @param refreshToken refresh_token
     * @return {@link OAuth2AccessToken}
     */
    public OAuth2AccessToken getToken(String refreshToken) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, GRANT_REFRESH_TOKEN);
        map.add(GRANT_REFRESH_TOKEN, refreshToken);
        return getOAuth2AccessToken(map);
    }

    /**
     * 根据userId 查找该用户 的角色
     *
     * @param userId 用户id
     * @return {@link Set<SystemRole>}
     */
    public Set<SystemRole> findRolesByUserId(Long userId) {
        SystemUser one = repository.findOne(userId);
        Set<SystemRole> systemRoles = one.getSystemRoles();
        return filterDefaultRole(systemRoles);

    }

    public Map<Long, Set<SystemRole>> findRolesInUserIds(Set<Long> userIds) {
        List<SystemUser> users = repository.findByIdIn(userIds);

        return users.stream().collect(Collectors.toMap(SystemUser::getId, systemUser -> {
            Set<SystemRole> systemRoles = systemUser.getSystemRoles();
            return filterDefaultRole(systemRoles);
        }));
    }

    /**
     * 对app 用户的 手机号 绑定或更换操作
     *
     * @param userId  app 用户id
     * @param encoder {@link SystemUserNameEncoder}
     */
    public void bindOrUpdateAppUsername(Long userId, SystemUserNameEncoder encoder) {
        SystemUser one = repository.findOne(userId);
        one.setUsername(encoder.encodeUsername());
    }

    /**
     * 查看app 用户是否已设置密码
     *
     * @param appUserId AppUser Id
     * @return 已设置 true ；未设置为false
     */
    public boolean checkAppUserHasPassword(Long appUserId) {
        SystemUser one = repository.findOne(appUserId);
        //是否是默认密码
        return !Strings.isNullOrEmpty(one.getPassword()) && !PasswordEncoderUtils.isMatches(DEFAULT_PASSWORD, one.getPassword());
    }

    /**
     * 对网页用户的 密码和 role 角色的更新
     *
     * @param rawPassword 密码
     * @param nameEncoder {@link SystemUserNameEncoder}
     * @param userId      用户id
     * @param newRoles    新的角色
     * @return 是否有更新密码或角色
     */
    public boolean isWebUserPasswordAndRolesUpdated(String rawPassword, SystemUserNameEncoder nameEncoder, Long userId, Set<SystemRole> newRoles) {
        boolean isUpdate = false;
        SystemUser systemUser = repository.findOne(userId);
        if (!Strings.isNullOrEmpty(rawPassword)) {
            boolean matches = PasswordEncoderUtils.isMatches(rawPassword, systemUser.getPassword());
            if (!matches) {
                systemUser.setPassword(PasswordEncoderUtils.encodePassword(rawPassword));
                isUpdate = true;
            }
        }
        if (newRoles != null) {
            Set<SystemRole> roles = systemUser.getSystemRoles();
            boolean b = roles.containsAll(newRoles);
            //之前的角色已包含现在需要新的角色，则不需要更新
            if (!b) {
                systemUser.setSystemRoles(newRoles);
                isUpdate = true;
            }
        }
        systemUser.setUsername(nameEncoder.encodeUsername());
        return isUpdate;
    }

    /**
     * 设置密码
     *
     * @param userId   用户id
     * @param password 密码
     */
    public void setPassword(Long userId, String password) {
        SystemUser systemUser = repository.findOne(userId);
        systemUser.setPassword(PasswordEncoderUtils.encodePassword(password));
    }


    /**
     * 重置密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     */
    public void resetPassword(Long userId, String newPassword) {
        SystemUser systemUser = repository.findOne(userId);
        systemUser.setPassword(PasswordEncoderUtils.encodePassword(newPassword));
        //移除token和refresh_token
        tokenManageService.removeTokenAndRefreshToken(systemUser.getUsername());
    }


    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void changeUserPassword(Long userId, String oldPassword, String newPassword) {
        SystemUser systemUser = repository.findOne(userId);
        String encodePassword = systemUser.getPassword();

        boolean matches = PasswordEncoderUtils.isMatches(oldPassword, encodePassword);

        if (!matches) {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, CommonArgumentsValidConstant.PASSWORD_ERROR_MSG);
        }
        systemUser.setPassword(PasswordEncoderUtils.encodePassword(newPassword));
        //移除token和refresh_token
        tokenManageService.removeTokenAndRefreshToken(systemUser.getUsername());
    }

    /**
     * 用户登出
     *
     * @param userId 用户id
     */
    public void logout(Long userId) {

        SystemUser systemUser = repository.findOne(userId);
        //移除token和refresh_token
        tokenManageService.removeTokenAndRefreshToken(systemUser.getUsername());

    }


    public void deleteSystemUser(Long userId) {
        SystemUser one = repository.findOne(userId);
        //删除Token和refresh_token
        tokenManageService.removeTokenAndRefreshToken(one.getUsername());
        repository.delete(one);
    }


    private URI getUri() {
        String port = environment.getProperty(LOCAL_SERVER_PORT);
        String contextPath = environment.getProperty(LOCAL_SERVER_CONTEXT_PATH);
        String url = LOCALHOST + port + (StringUtils.hasText(contextPath) ? contextPath : "") + OAUTH_TOKEN;
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
            throw new SystemServiceException(ErrorMessageEnum.SYSTEM_UNKNOWN_ERROR);
        }
    }

    /**
     * 获取 Basic请求头
     *
     * @return {@link HttpHeaders}
     */
    private HttpHeaders getHttpHeaders() {
        return HttpUtils.createBasicHeaders(CLIENT, SECRET);
    }

    private OAuth2AccessToken getOAuth2AccessToken(MultiValueMap<String, String> map) {
        URI uri = getUri();
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(map, headers);

        return HttpUtils.post(uri, body, OAuth2AccessToken.class);
    }

    /**
     * 过滤掉 系统默认设置的角色
     *
     * @param systemRoles {@link Set<SystemRole>} 角色的集合
     * @return Set<SystemRole> 除去了系统默认设置的角色 的集合
     */
    private Set<SystemRole> filterDefaultRole(Set<SystemRole> systemRoles) {
        return systemRoles.stream()
                .filter(role -> RoleInSystem.notContainsNormalRole(role.getRoleName()))
                .collect(Collectors.toSet());
    }

}
