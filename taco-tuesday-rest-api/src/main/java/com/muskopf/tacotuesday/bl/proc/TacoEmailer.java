package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.mailgun.emailsender.EmailSender;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.proc.EmailBuilder;
import com.muskopf.tacotuesday.config.TacoTuesdayApiProperties;
import com.muskopf.tacotuesday.domain.FullOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Component
public class TacoEmailer {
    private static final String TT_API_HEADER = "[Taco Tuesday API @ %s]: ";
    private EmailBuilder emailBuilder;
    private EmailSender emailSender;
    private Logger logger = LoggerFactory.getLogger(TacoEmailer.class);
    private boolean sendEmails;

    @Autowired
    public TacoEmailer(EmailBuilder emailBuilder, EmailSender emailSender, TacoTuesdayApiProperties properties) {
        this.emailBuilder = emailBuilder;
        this.emailSender = emailSender;
        this.sendEmails = properties.isEmailEnabled();
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        sendStartupEmail();
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

    private void sendEmail(String topic, String subject, String body) {
        if (!shouldSendEmail(topic)) {
            return;
        }

        Email email = getEmail(subject, body);

        emailSender.sendEmail(email);
    }

    public void sendStartupEmail() {
        sendEmail("startup", "Startup", "The TacoTuesday API service has started!");
    }

    public void sendOrderSubmittedEmail(FullOrder fullOrder) {
        sendEmail("order submission",
                "New Order Submitted!",
                "The following Full Order has been submitted:\n" + fullOrder);
    }

    public void sendExceptionEmail(Exception e) {
        String sep = System.lineSeparator();

        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);

        StringBuilder stackTrace = new StringBuilder();
        for (String line : sWriter.toString().split("\n")) {
            stackTrace.append("\t\t").append(line);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("The following exception has occurred:").append(sep);
        sb.append("\tType: ").append(e.getClass().getSimpleName()).append(sep);
        sb.append("\tCause: ").append(e.getCause()).append(sep);
        sb.append("\tMessage: ").append(e.getMessage()).append(sep);
        sb.append("\tStacktrace:").append(sep).append(stackTrace.toString());

        sendEmail("exception",
                "An Exception has Occurred!",
                sb.toString());
    }
}
