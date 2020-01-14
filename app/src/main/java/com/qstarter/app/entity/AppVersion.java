package com.qstarter.app.entity;

import com.qstarter.core.entity.BaseEntity;
import com.qstarter.core.enums.DeviceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author peter
 * date: 2019-12-20 08:54
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class AppVersion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum device;

    //android版本号
    private String version;

    //版本code 一直累加
    private Integer versionCode = 0;

    //是否强制更新
    private Boolean forceUpdate;

    private String androidApkPath;

    private String iosUrl;

    private String title;

    private String content;


    public static AppVersion android(String version, Integer versionCode, boolean forceUpdate,
                                     String androidApkPath, String title, String content) {
        AppVersion appVersion = new AppVersion();
        appVersion.setDevice(DeviceTypeEnum.ANDROID);
        appVersion.setVersion(version);
        appVersion.setVersionCode(versionCode);
        appVersion.setForceUpdate(forceUpdate);
        appVersion.setAndroidApkPath(androidApkPath);
        appVersion.setTitle(title);
        appVersion.setContent(content);
        return appVersion;
    }

    public static AppVersion ios(String version, boolean forceUpdate,
                                 String title, String content,String iosUrl) {
        AppVersion appVersion = new AppVersion();
        appVersion.setDevice(DeviceTypeEnum.IOS);
        appVersion.setVersion(version);
        appVersion.setForceUpdate(forceUpdate);
        appVersion.setTitle(title);
        appVersion.setContent(content);
        appVersion.setIosUrl(iosUrl);
        return appVersion;
    }
}
