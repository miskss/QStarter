package com.qstarter.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author peter
 * date: 2019-12-05 17:47
 **/
@Configuration("projectConfig")
@ConfigurationProperties(prefix = "project")
@Data
public class ProjectConfig {
    private String projectName;
    private String version;
    private String developName;
    private String developUrl;
}
