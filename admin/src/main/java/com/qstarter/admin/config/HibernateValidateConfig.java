package com.qstarter.admin.config;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * hibernate 参数验证
 *
 * @author peter
 * date 2021/1/7 14:35
 */
@Configuration
public class HibernateValidateConfig {

    @Bean
    public Validator validator() {
        //failFast：true  快速失败返回模式(只要有一个验证失败，则返回)
        // false 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .addProperty(HibernateValidatorConfiguration.FAIL_FAST, "true")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        //设置validator模式为快速失败返回
        postProcessor.setValidator(validator());
        return postProcessor;
    }
}
