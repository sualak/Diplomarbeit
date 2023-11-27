package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.EmailTakenException;
import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.factories.UserFactory;
import at.dertanzbogen.api.factories.UserFactory.UserWithToken;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.security_token_auth.PasswordService;
//import at.dertanzbogen.api.security.PasswordService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationService.class);

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final EmailService emailService;

    /**
     * Registers a new user.
     *
     * @param userDTO the user to register
     * @return the registered user
     *
     * @throws EmailTakenException if the email is already taken
     * @throws IllegalArgumentException if the user is already verified
     * @throws IllegalArgumentException if the user is already enabled
     *
     */
    public String register(UserRegistrationCommand userDTO) {
        LOGGER.info("Registering user with email: {}", userDTO.email());

        if(userRepository.existsByEmailEmail(userDTO.email())) {
            throw new EmailTakenException("Email already taken");
        }

        UserWithToken userWithToken = UserFactory.of(userDTO).encoder(passwordService).needsVerification();

        userRepository.save(userWithToken.user());
        emailService.sendEmailVerification(userWithToken.user(), userWithToken.token());

        LOGGER.info("User {} registered successfully. Email verification {} pending.",
                userWithToken.user().getId(), userWithToken.user().getEmail().getEmail());

        return userWithToken.user().getId();
    }

    /**
     * Verifies a user.
     *
     * @param verificationCommand the verification command
     *
     * @throws IllegalArgumentException if the user is not found
     * @throws IllegalArgumentException if the token is not found
     * @throws IllegalArgumentException if the token is expired
     * @throws IllegalArgumentException if the token is already used
     *
     */
    public void verifyUser(Commands.VerificationCommand verificationCommand) {
        LOGGER.info("Verifying user {} with token {}", verificationCommand.userId(), verificationCommand.tokenId());

        User user = userRepository.findById(verificationCommand.userId()).orElseThrow(() ->
                new IllegalArgumentException("User not found"));

        user.setEmail(new Email(user.getAccount().verifyVerificationToken(verificationCommand.tokenId())));
        user.getAccount().setEnabled(true);
        userRepository.save(user);

        LOGGER.info("User {} verified successfully.", user.getId());
    }
}
