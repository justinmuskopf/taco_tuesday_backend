package com.muskopf.tacotuesday;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties
@Import(TacoTuesdayApiConfiguration.class)
public class TacoTuesdayApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TacoTuesdayApiApplication.class, args);
    }
}
