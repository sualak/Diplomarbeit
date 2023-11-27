package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.error.EmailTakenException;
import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.factories.UserFactory;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
//import at.dertanzbogen.api.security.PasswordService;
import at.dertanzbogen.api.security_token_auth.PasswordService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminRegistrationService {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminRegistrationService.class);

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final EmailService emailService;


    public String registerAsAdmin(Commands.UserRegistrationCommand userDTO) {
        LOGGER.info("Registering user with email: {}", userDTO.email());

        if(userRepository.existsByEmailEmail(userDTO.email())) {
            throw new EmailTakenException("Email already taken");
        }

        UserFactory.UserWithToken userWithToken = UserFactory.of(userDTO).encoder(passwordService).needsVerificationAsAdmin();

        userRepository.save(userWithToken.user());
        emailService.sendEmailVerification(userWithToken.user(), userWithToken.token());

        LOGGER.info("User {} registered successfully. Email verification {} pending.",
                userWithToken.user().getId(), userWithToken.user().getEmail().getEmail());

        return userWithToken.user().getId();
    }
}
