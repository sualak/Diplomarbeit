package at.dertanzbogen.api.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static at.dertanzbogen.api.foundation.SpringUtils.getBean;


@Component
@ConditionalOnBean(JavaMailSender.class)
public class JavaMailSenderImpl implements MailSender{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerMailSenderImpl.class);

    private final JavaMailSender mailSender;

    public JavaMailSenderImpl(ApplicationContext context) {
        this.mailSender = getBean(context, JavaMailSender.class);
    }

    @Override
    public void send(EmailDTO email) {
        try {
            LOGGER.debug("Sending email via JavaMailSenderImpl to: {} ", email.recipient());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.recipient());
            message.setSubject(email.subject());
            message.setText(email.body());
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to: " + email.recipient(), e);
        }

    }
}
