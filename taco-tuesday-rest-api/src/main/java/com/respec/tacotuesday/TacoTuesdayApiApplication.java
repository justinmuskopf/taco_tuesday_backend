package com.respec.tacotuesday;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TacoTuesdayConfiguration.class)
public class TacoTuesdayApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoTuesdayApiApplication.class, args);
	}

}
