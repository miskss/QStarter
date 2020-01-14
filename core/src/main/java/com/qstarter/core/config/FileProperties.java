package com.qstarter.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author peter
 * date: 2019-09-23 14:43
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.upload")
public class FileProperties {

    private String root = "/file";

    private String picture = "/picture";

    private String apk = "/apk";

    public String getImgPath() {
        return root + picture;
    }

    public String getApkPath(){
        return root + apk;
    }
}
