package com.qstarter.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author peter
 * date: 2019-09-06 17:23
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Primary
@ComponentScan(basePackages = "com.*")
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private AdminPermissionEvaluator adminPermissionEvaluator;

    public MethodSecurityConfig(AdminPermissionEvaluator adminPermissionEvaluator) {
        this.adminPermissionEvaluator = adminPermissionEvaluator;
    }


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(adminPermissionEvaluator);
        return expressionHandler;
    }
}