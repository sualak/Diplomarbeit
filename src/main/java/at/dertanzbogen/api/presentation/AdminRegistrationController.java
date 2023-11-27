package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.presentation.DTOs.Commands;
//import at.dertanzbogen.api.security.PasswordService;
import at.dertanzbogen.api.security_token_auth.PasswordService;
import at.dertanzbogen.api.service.AdminRegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.ResponseEntity.created;
@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminRegistrationController {

    private final AdminRegistrationService adminRegistrationService;
    private final PasswordService passwordService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/registration")
    public ResponseEntity<Void> registerAsAdmin(@Valid @RequestBody Commands.UserRegistrationCommand userRegistrationCommand) {
        logger.info("Creating user: " + userRegistrationCommand);
        String id = adminRegistrationService
                .registerAsAdmin(userRegistrationCommand);
        return created(URI.create("/api/user/registration/"+id))
                .build();
    }
}
