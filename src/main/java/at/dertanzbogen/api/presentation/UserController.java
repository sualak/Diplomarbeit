package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;


@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    // /user/login
    // rename to getData
    @GetMapping("/getData")
    public Views.UserLoginView login(@AuthenticationUser User user,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "50") int size,
                                     @RequestParam(defaultValue = "0") int notificationPage,
                                     @RequestParam(defaultValue = "50") int notificationSize) {
        logger.info("Getting user with email: " + user.getEmail().getEmail());
        Pageable pageable = PageRequest.of(page, size);
        Pageable notificationPageable = PageRequest.of(notificationPage, notificationSize, Sort.by("createdAt").descending());
        return userService.login(user, pageable, notificationPageable);
    }

    @DeleteMapping()
    public String deleteMe(@AuthenticationUser User user) {
        logger.info("Deleting user with id: " + user.getId());
        userService.deleteUserById(user.getId());
        return "User deleted with id: " + user.getId();
    }


    @PostMapping()
    public Views.UserView updateMe(@AuthenticationUser User userValid , @RequestBody Commands.UserUpdateCommand user) {
        logger.info("Updating user with id: " + userValid.getId());
        return userService.updateUser(userValid, user);
    }

    @PostMapping("/email")
    public void updateMeEmailRequest(@AuthenticationUser User userValid , @RequestBody Commands.UserUpdateEmailCommand emailCommand) {
        logger.info("Updating user with id: " + userValid.getId());
        userService.updateUserEmail(userValid, emailCommand);
    }

    @GetMapping("/email/verify")
    public void updateMeEmailVerify(@AuthenticationUser User userValid , @Valid @ModelAttribute Commands.VerificationCommand verificationCommand) {
        logger.info("Updating user email from user with id: " + userValid.getId());
        userService.updateUserEmailVerify(userValid, verificationCommand);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, path = "/addProfilePicture")
    public Views.UserView addProfilePicture(@AuthenticationUser User user, @Valid @RequestPart Commands.UploadMediaCommand uploadMediaCommand,
                                                 @RequestPart(name = "media", required = false) MultipartFile mediaFile) {
        logger.info("Adding profile picture to user with id: " + user.getId());
        return userService.addProfilePicture(user, uploadMediaCommand, mediaFile);
    }

    @DeleteMapping("/removeProfilePicture")
    public Views.UserView removeProfilePicture(@AuthenticationUser User user) {
        logger.info("Removing profile picture from user with id: " + user.getId());
        return userService.removeProfilePicture(user);
    }

    @PostMapping("/addPartner")
    public void addPartner(@AuthenticationUser User user, @Valid @RequestBody Commands.PartnerRequestCommand partnerRequestCommand)
    {
        logger.info("sending partner request from user with id: " + user.getId() + "to user with email: " + partnerRequestCommand.email());
        userService.addPartner(user, partnerRequestCommand);
    }

    @DeleteMapping("/deletePartner")
    public Views.NotificationUserView deletePartner(@AuthenticationUser User user, @RequestParam String partnerId)
    {
        logger.info("deleting partner from user with id: " + user.getId() + " and partner with id: " + partnerId);
        return userService.deletePartner(user, partnerId);
    }


//    // /user/email/{id}
//    @PutMapping("/email/{id}")
//    public User updateUserByEmail(@PathVariable("id") String id) {
//        logger.info("Updating user with validEmail: " );
//        return userService.emailValidated(id);
//    }
//
//    // /user/newemail/{id}
//    @PutMapping("/newemail/{id}")
//    public User updateUserByNewEmail(@PathVariable("id") String id, @RequestBody User user) {
//        logger.info("Updating user with newEmail: " );
//        return userService.updateNewEmail(id, user).orElseThrow(() -> new EmailTakenException("Email already taken"));
//    }

    // /user/login/{email}/{password}
//    @GetMapping("/login")
//    public User getUserByEmailAndPassword(@RequestParam("email") String email, @RequestParam("password") String password) throws UserNotFoundException {
//        logger.info("Getting user with email: " + email);
//        return userService.getUserByEmailAndPassword(new Email(email), new Password(password)).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
//    }


//    @GetMapping("/login")
//    public User getUserByEmailAndPassword(@RequestBody ObjectNode jsonNodes) {
//        logger.info("Getting user with email: " + jsonNodes.get("email").asText());
//        return userService.getUserByEmailAndPassword(new Email(jsonNodes.get("email").asText()), new Password(jsonNodes.get("password").asText()))
//                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + jsonNodes.get("email").asText()));
////        int strength = 10; // work factor of bcrypt
////        BCryptPasswordEncoder bCryptPasswordEncoder =
////                new BCryptPasswordEncoder(strength, new SecureRandom());
////        User user =  userService.getUserByEmail(new Email(jsonNodes.get("email").asText()))
////                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + jsonNodes.get("email").asText()));
////        if (bCryptPasswordEncoder.matches(jsonNodes.get("password").asText(), user.getPassword().getPassword())) {
////            return user;
////        } else {
////            throw new UserNotFoundException("User not found with email: " + jsonNodes.get("email").asText());
////        }
//    }

//    // /user/userGroup/{userGroup}
//    @GetMapping("/userGroup/{userGroup}")
//    public List<User> getAllUsersByUserGroup(@PathVariable("userGroup") String userGroup) {
//        logger.info("Getting all users with userGroup: " + userGroup);
//        return userService.getUsersByUserGroup(User.userGroup.valueOf(userGroup));
//    }
//
//    // /user/leader/{leader}
//    @GetMapping("/leader/{leader}")
//    public List<User> getAllUsersByLeader(@PathVariable("leader") boolean leader) {
//        logger.info("Getting all users with leader: " + leader);
//        return userService.getUsersByLeaderIs(leader);
//    }
//
//    // /user/helper/{helper}
//    @GetMapping("/helper/{helper}")
//    public List<User> getAllUsersByHelper(@PathVariable("helper") boolean helper) {
//        logger.info("Getting all users with helper: " + helper);
//        return userService.getUsersByHelperIs(helper);
//    }
//
//    // /user/helper/{helper}/leader/{leader}
//    @GetMapping("/helper/{helper}/leader/{leader}")
//    public List<User> getAllUsersByHelperAndLeader(@PathVariable("helper") boolean helper, @PathVariable("leader") boolean leader) {
//        logger.info("Getting all users with helper: " + helper + " and leader: " + leader);
//        return userService.getUsersByHelperIsAndLeaderIs(helper, leader);
//    }
//
//    // /user/firstName/{firstName}
//    @GetMapping("/firstName/{firstName}")
//    public List<User> getAllUsersByFirstName(@PathVariable("firstName") String firstName) {
//        logger.info("Getting all users with firstName: " + firstName);
//        return userService.getUsersByFirstName(firstName);
//    }
//
//    // /user/lastName/{lastName}
//    @GetMapping("/lastName/{lastName}")
//    public List<User> getAllUsersByLastName(@PathVariable("lastName") String lastName) {
//        logger.info("Getting all users with lastName: " + lastName);
//        return userService.getUsersByLastName(lastName);
//    }
//
//    // /user/firstName/{firstName}/lastName/{lastName}
//    @GetMapping("/firstName/{firstName}/lastName/{lastName}")
//    public List<User> getAllUsersByFirstNameAndLastName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
//        logger.info("Getting all users with firstName: " + firstName + " and lastName: " + lastName);
//        return userService.getUsersByFirstNameAndLastName(firstName, lastName);
//    }
}
