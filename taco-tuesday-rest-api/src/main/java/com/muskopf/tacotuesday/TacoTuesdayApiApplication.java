package com.muskopf.tacotuesday;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TacoTuesdayApiConfiguration.class)
public class TacoTuesdayApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TacoTuesdayApiApplication.class, args);
    }
}
