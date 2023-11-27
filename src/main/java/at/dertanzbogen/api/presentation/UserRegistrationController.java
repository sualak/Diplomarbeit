package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.EmailTakenException;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.security_token_auth.PasswordService;
//import at.dertanzbogen.api.security.PasswordService;
import at.dertanzbogen.api.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;
    private final PasswordService passwordService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    // /user/registration
    /**
     * Registers a new user.
     *
     * @param userRegistrationCommand the user to register
     * @return the registered user
     *
     * @throws EmailTakenException if the email is already taken
     * @throws IllegalArgumentException if the user is already verified
     * @throws IllegalArgumentException if the user is already enabled
     *
     */
    @PostMapping("/registration")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationCommand userRegistrationCommand) {
        logger.info("Creating user: " + userRegistrationCommand);
        String id = userRegistrationService
                .register(userRegistrationCommand);
        return created(URI.create("/api/user/registration/"+id))
                .build();
    }

    // /user/verify?userId=123&tokenId=123
    /**
     * Verifies a user.
     *
     * @param verificationCommand the verification command
     *
     * @throws IllegalArgumentException if the user is already verified
     * @throws IllegalArgumentException if the user is already enabled
     *
     */
    @GetMapping("/verify")
    public void verify(@Valid @ModelAttribute Commands.VerificationCommand verificationCommand) {
        logger.info("Verifying user with token: " + verificationCommand.tokenId());
        userRegistrationService.verifyUser(verificationCommand);
    }
}
