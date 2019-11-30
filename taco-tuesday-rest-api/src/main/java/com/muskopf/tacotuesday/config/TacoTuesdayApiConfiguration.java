package com.muskopf.tacotuesday.config;

import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.muskopf.tacotuesday.bl.repository")
@ComponentScan(basePackages = "com.muskopf.tacotuesday")
@EnableConfigurationProperties(TacoTuesdayApiProperties.class)
@Import(EmailSenderConfiguration.class)
public class TacoTuesdayApiConfiguration {
}


