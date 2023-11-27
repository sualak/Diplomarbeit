package at.dertanzbogen.api.email;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Service for sending emails.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final MailSender mailSender;
    public static final String USER_ID_PARAM = "userId";
    public static final String TOKEN_PARAM = "tokenId";
    public static final String VERIFICATION_LINK = "%s/api/user/verify?%s=%s&%s=%s";


    //@Async
    /**
     * Sends an email verification to the given user.
     *
     * @param user the user to send the verification to
     * @param token the token to verify the user
     */
    public void sendEmailVerification(User user, String token)
    {
        LOGGER.info("Sending Email Verification to {}", user.getEmail());

        EmailDTO email = new EmailDTO(
                user.getEmail().getEmail(),
                getVerificationSubject(),
                getVerificationBody(user, token)
        );

        mailSender.send(email);
    }


    // Subject/Body Helpers -----------------------------------------------------

    private String getVerificationSubject()
    {
        return "Please verify your email";
    }

    private String getVerificationBody(User user, String token)
    {
        return String.format(
                "Please verify your email by clicking on the following link:\n" +
                        VERIFICATION_LINK,
                "http://localhost:8080", USER_ID_PARAM, user.getId(), TOKEN_PARAM, token);
    }

    public void sendCourseBookedEmail(Course course, User user) {
        LOGGER.info("Sending Course Booked Email to {}", user.getEmail());

        EmailDTO email = new EmailDTO(
                user.getEmail().getEmail(),
                getCourseBookedSubject(),
                getCourseBookedBody(course)
        );

        mailSender.send(email);
    }

    private String getCourseBookedSubject() {
        return "Course Booked";
    }

    private String getCourseBookedBody(Course course) {
        LocalDateTime datetime = LocalDateTime.ofInstant(course.getStartsAt(), ZoneOffset.UTC);
        String formatted = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss").format(datetime);


        return String.format(
                "You have booked the course %s on %s",
                course.getTheme(),
                formatted);
    }
}
