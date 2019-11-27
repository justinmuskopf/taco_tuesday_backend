package com.muskopf.tacotuesday;

import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableConfigurationProperties({
        TacoTuesdayApiConfiguration.class
})
@ComponentScan(basePackages = {
        "com.muskopf.tacotuesday",
        "com.muskopf.mailgun.emailsender",
})
public class TacoTuesdayApiApplication {
    private TacoEmailer tacoEmailer;

    @Autowired
    public TacoTuesdayApiApplication(TacoEmailer tacoEmailer) {
        this.tacoEmailer = tacoEmailer;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        tacoEmailer.sendStartupEmail();
    }

    public static void main(String[] args) {
        SpringApplication.run(TacoTuesdayApiApplication.class, args);
    }
}
