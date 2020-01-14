package com.qstarter.app.service;

import com.google.common.base.Strings;
import com.qstarter.app.entity.AppVersion;
import com.qstarter.app.model.dto.AppVersionDTO;
import com.qstarter.app.repository.AppVersionRepository;
import com.qstarter.core.constant.CacheName;
import com.qstarter.core.enums.DeviceTypeEnum;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.CommonUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author peter
 * date: 2019-12-20 08:58
 **/
@Service
@CacheConfig(cacheNames = CacheName.APP_VERSION)
public class AppVersionService {

    private static final String IOS = "'ios'";
    private static final String ANDROID = "'android'";
    private AppVersionRepository repository;

    public AppVersionService(AppVersionRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(key = IOS)
    public void uploadIos(AppVersionDTO dto) {
        String path = dto.getIosUrl();
        if (Strings.isNullOrEmpty(path)) {
            throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION, "ios 的下载地址不能为空");
        }
        AppVersion latestOne = findIosLatestOne();
        if (latestOne != null) {
            //比较最新的版本号和库中相比较
            String latestOneVersion = latestOne.getVersion();
            boolean b = CommonUtils.compareAppVersion(latestOneVersion, dto.getVersion());
            if (!b) {
                throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION, "上传的版本号必须大于库中最新的版本号,库中最新的版本号为：" + latestOneVersion);
            }
        }
        AppVersion ios = AppVersion.ios(dto.getVersion(), dto.getForceUpdate(), dto.getTitle(),
                dto.getContent(), dto.getIosUrl());
        repository.save(ios);
    }

    @CacheEvict(key = ANDROID)
    public void uploadAndroid(AppVersionDTO dto) {
        String path = dto.getAndroidApkPath();
        if (Strings.isNullOrEmpty(path)) {
            throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION, "android apk 的下载地址不能为空");
        }
        AppVersion latestOne = findAndroidLatestOne();
        if (latestOne != null) {
            //比较最新的版本号和库中相比较
            String latestOneVersion = latestOne.getVersion();
            boolean b = CommonUtils.compareAppVersion(latestOneVersion, dto.getVersion());
            if (!b) {
                throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION, "上传的版本号必须大于库中最新的版本号,库中最新的版本号为：" + latestOneVersion);
            }
            Integer versionCode = latestOne.getVersionCode();
            if (versionCode >= dto.getVersionCode())
                throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION, "上传的版本code值必须大于库中最新的code值，库中最新的code为：" + versionCode);

        }
        AppVersion android = AppVersion.android(dto.getVersion(), dto.getVersionCode(), dto.getForceUpdate(), dto.getAndroidApkPath(),
                dto.getTitle(), dto.getContent());
        repository.save(android);
    }

    @Cacheable(key = ANDROID)
    public AppVersion findAndroidLatestOne() {
        return repository.findByDeviceEqualsOrderByCreateTimeDesc(DeviceTypeEnum.ANDROID)
                .stream().findFirst()
                .orElse(null);
    }

    @Cacheable(key = IOS)
    public AppVersion findIosLatestOne() {
        return repository.findByDeviceEqualsOrderByCreateTimeDesc(DeviceTypeEnum.IOS)
                .stream().findFirst()
                .orElse(null);
    }
}
