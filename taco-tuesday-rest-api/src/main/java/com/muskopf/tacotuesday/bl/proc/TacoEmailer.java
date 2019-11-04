package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.mailgun.emailsender.EmailSender;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.proc.EmailBuilder;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.muskopf.tacotuesday.domain.FullOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TacoEmailer {
    private static final String TT_API_HEADER = "[Taco Tuesday API @ %s]: ";
    private EmailBuilder emailBuilder;
    private EmailSender emailSender;
    private Logger logger = LoggerFactory.getLogger(TacoEmailer.class);
    private boolean sendEmails;

    @Autowired
    public TacoEmailer(EmailBuilder emailBuilder, EmailSender emailSender, TacoTuesdayApiConfiguration configuration) {
        this.emailBuilder = emailBuilder;
        this.emailSender = emailSender;
        this.sendEmails = configuration.shouldSendEmails();
    }

    private static String getEmailHeader() {
        return String.format(TT_API_HEADER, LocalDateTime.now().toString());
    }

    private boolean shouldSendEmail(String topic) {
        if (!sendEmails) {
            logger.info("Not sending " + topic + " email (Disabled by configuration properties)!");
        } else {
            logger.info("Sending " + topic + " email!");
        }

        return sendEmails;
    }

    private Email getEmail(String subject, String body) {
        return emailBuilder.builder()
                .useDefaultRecipients()
                .useDefaultSender()
                .subject(getEmailHeader() + subject)
                .body(body)
                .build();
    }

    public void sendStartupEmail() {
        if (!shouldSendEmail("startup")) {
            return;
        }

        Email email = getEmail(
                "Startup",
                "The TacoTuesday API service has started!"
        );

        emailSender.sendEmail(email);
    }

    public void sendOrderSubmittedEmail(FullOrder fullOrder) {
        if (!shouldSendEmail("order submission")) {
            return;
        }

        Email email = getEmail(
                "New Order Submitted!",
                "The following Full Order has been submitted:\n" + fullOrder
        );

        emailSender.sendEmail(email);
    }
}
