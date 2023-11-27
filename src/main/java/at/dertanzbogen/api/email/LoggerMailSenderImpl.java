package at.dertanzbogen.api.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(JavaMailSender.class)
public class LoggerMailSenderImpl implements MailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerMailSenderImpl.class);

    private EmailDTO email;

    @Override
    public void send(EmailDTO email) {
        this.email = email;

        LOGGER.debug("Sending email via LoggerMailSenderImpl to: {} ", email.recipient());

        LOGGER.info(String.format("RECIPIENT: %s/nSUBJECT: %s/nTEXT: %s", email.recipient(), email.subject(), email.body()));
    }

    public EmailDTO getEmailDTO() {
        return email;
    }
}
