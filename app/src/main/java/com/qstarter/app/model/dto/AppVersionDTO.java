package com.qstarter.app.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author peter
 * date: 2019-12-20 09:15
 **/
@Data
public class AppVersionDTO {

    @NotBlank
    @NotNull
    private String device;

    //android版本号
    @NotBlank(message = "版本号不能为空")
    private String version;

    //版本code 一直累加
    private Integer versionCode = 0;

    //是否强制更新
    private Boolean forceUpdate = false;

    private String androidApkPath;

    @NotBlank(message = "本次更新的标题不能为空")
    @Size(min = 1, max = 40, message = "更新标题的长度必须在1~40字符之间")
    private String title;

    @NotBlank(message = "本次更新的内容不能为空")
//    @Size(min = 1, max = 500, message = "更新内容的长度必须在1~240字符之间")
    private String content;

    private String iosUrl;
}
