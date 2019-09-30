package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.mailgun.emailsender.EmailSender;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.proc.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TacoEmailer {
    private final String TT_API_HEADER = "[Taco Tuesday API]: ";
    private EmailBuilder emailBuilder;
    private EmailSender emailSender;

    @Autowired
    public TacoEmailer(EmailBuilder emailBuilder, EmailSender emailSender) {
        this.emailBuilder = emailBuilder;
        this.emailSender = emailSender;
    }

    public void sendStartupEmail() {
        Email email = emailBuilder.builder()
                .useDefaultRecipients()
                .useDefaultSender()
                .subject(TT_API_HEADER + "Startup @ " + LocalDateTime.now().toString())
                .body("The TacoTuesday API service has started!")
                .build();

        emailSender.sendEmail(email);
    }
}
