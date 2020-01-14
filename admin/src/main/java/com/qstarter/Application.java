package com.qstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author peter
 * date: 2019-09-03 16:56
 **/
@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = "com.*")
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages = "com.*")
@EnableCaching
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
