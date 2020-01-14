package com.qstarter.app.model.vo;

import com.qstarter.app.entity.AppVersion;
import com.qstarter.app.utils.UrlUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author peter
 * date: 2019-12-20 17:03
 **/
@Data
@ApiModel
public class AppVersionVO {

    //android版本号
    private String version;

    //版本code 一直累加
    private Integer versionCode;

    //是否强制更新
    @ApiModelProperty("是否强制更新")
    private Boolean forceUpdate;

    @ApiModelProperty("android apk 下载地址")
    private String androidApkPath;

    private String title;

    @ApiModelProperty("更新的内容，格式：富文本")
    private String content;
    @ApiModelProperty("ios下载地址")
    private String iosUrl;

    public static AppVersionVO fromEntity(AppVersion appVersion){
        AppVersionVO appVersionVO = new AppVersionVO();
        appVersionVO.setVersion(appVersion.getVersion());
        appVersionVO.setVersionCode(appVersion.getVersionCode());
        appVersionVO.setForceUpdate(appVersion.getForceUpdate());
        appVersionVO.setAndroidApkPath(UrlUtils.toHttpUrl(appVersion.getAndroidApkPath()));
        appVersionVO.setTitle(appVersion.getTitle());
        appVersionVO.setContent(appVersion.getContent());
        appVersionVO.setIosUrl(appVersion.getIosUrl());
        return appVersionVO;}
}
