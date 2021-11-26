package com.qstarter.admin.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * create at 2021/11/26 09:20
 *
 * @author peter
 */
@Configuration
public class SpringQuartzScheduler {

    private final DataSourceProperties properties;

    public SpringQuartzScheduler(DataSourceProperties properties) {
        this.properties = properties;
    }
//
//    @Bean("quartzDataSource")
//    @QuartzDataSource
//    public DataSource quartzDataSource() {
//        return DataSourceBuilder.create(properties.getClassLoader())
//                .url(properties.getUrl())
//                .username(properties.getUsername())
//                .password(properties.getPassword())
//                .driverClassName(properties.getDriverClassName())
//                .build();
//    }
}
