package com.fx.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description TODO   开启hibernate快速校验失败策略
 * @date 2020-04-04 12:40
 */
@Configuration
public class FastValidFailConfig {

    @Bean
    @Primary
    public Validator enableFastFail() {

        return Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    }
}
