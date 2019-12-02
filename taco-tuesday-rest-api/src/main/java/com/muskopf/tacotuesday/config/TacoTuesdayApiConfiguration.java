package com.muskopf.tacotuesday.config;

import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.muskopf.tacotuesday.bl.repository")
@ComponentScan(basePackages = "com.muskopf.tacotuesday")
@EnableConfigurationProperties(TacoTuesdayApiProperties.class)
@Import(EmailSenderConfiguration.class)
public class TacoTuesdayApiConfiguration {
    /**
     * Define a bean for the JavaX Validator
     * This, in combination with {@link #methodValidationPostProcessor()},
     * allows our custom validators to {@code @Autowire} in DAOs and other components
     *
     * @return The "custom" validator
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();//.getValidator();
    }

    /**
     * Define a bean for the {@code MethodValidationPostProcessor}
     * Uses the Validator defined by {@link #validator()} to create a "custom"
     * validation post processor so that the custom validators can autowire dependencies
     *
     * @return The "custom" post processor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());

        return postProcessor;
    }
}


