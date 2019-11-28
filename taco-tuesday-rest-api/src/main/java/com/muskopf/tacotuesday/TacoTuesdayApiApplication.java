package com.muskopf.tacotuesday;

import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({
        TacoTuesdayApiConfiguration.class
})
@ComponentScan(basePackages = {
        "com.muskopf.tacotuesday",
        "com.muskopf.mailgun.emailsender",
})
public class TacoTuesdayApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TacoTuesdayApiApplication.class, args);
    }
}
