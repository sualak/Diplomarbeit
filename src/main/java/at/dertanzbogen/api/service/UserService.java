package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.Password;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.EmailNotValidException;
import at.dertanzbogen.api.domain.main.error.EmailTakenException;
import at.dertanzbogen.api.domain.main.error.PartnerRequestException;
import at.dertanzbogen.api.domain.main.error.UserNotFoundException;
import at.dertanzbogen.api.domain.validation.Ensure;
import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.persistent.NotificationRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.NotificationUserViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserLoginViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.security_token_auth.PasswordService;
//import at.dertanzbogen.api.security.PasswordService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {



    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private final CourseUserService courseUserService;
    private final MediaService mediaService;
    private final NotificationRepository notificationRepository;
    private final NotificationUserService notificationUserService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final UserViewMapper mapper = UserViewMapper.INSTANCE;
    private static final UserLoginViewMapper loginMapper = UserLoginViewMapper.INSTANCE;


    public Views.UserLoginView login(User user, Pageable pageable, Pageable notificationPageable)
    {
        LOGGER.info("User logged in: {}", user);

        return loginMapper.convert(user,userRepository.findAllById(user.getPartner().getPartners()),
                notificationUserService.getAllMyNotifications(user, notificationPageable),
                courseUserService.getMyBookedCoursesStatus(user, pageable, CourseBaseEntity.CourseStatus.OPEN.name()));
    }


    //----new

    public void deleteUserById(String id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    public Views.UserView updateUser(User userValid, Commands.UserUpdateCommand user) {
        if(Objects.nonNull(user.personal().getFirstName()) && !"".equalsIgnoreCase(user.personal().getFirstName())){
            userValid.getPersonal().setFirstName(user.personal().getFirstName());
        }
        if(Objects.nonNull(user.personal().getLastName()) && !"".equalsIgnoreCase(user.personal().getLastName())){
            userValid.getPersonal().setLastName(user.personal().getLastName());
        }
        if(Objects.nonNull(user.personal().getProfilPicture()) && !"".equalsIgnoreCase(user.personal().getProfilPicture().fileName())){
            userValid.getPersonal().setProfilPicture(user.personal().getProfilPicture());
        }
        if(Objects.nonNull(user.password()) && !"".equalsIgnoreCase(user.password())){
            userValid.setPassword(new Password(passwordService.encode(user.password()).getPassword()));
        }
        if(Objects.nonNull(user.isHelper())) {
            userValid.setHelper(user.isHelper());
        }
        if(Objects.nonNull(user.isLeader())) {
            userValid.setLeader(user.isLeader());
        }

        return mapper.convert(userRepository.save(userValid));
    }

    public void updateUserEmail(User userValid, Commands.UserUpdateEmailCommand emailCommand) {
        if(userRepository.existsByEmailEmail(emailCommand.email())) {
            throw new EmailTakenException("Email already taken");
        }

        if(!Ensure.isValidEmail(emailCommand.email())){
            throw new EmailNotValidException("Email not valid");
        }

        String token = userValid.getAccount().generateVerificationToken(emailCommand.email());

        LOGGER.info("Email {} update with email: {}", userValid.getEmail().getEmail(), emailCommand.email());

        userRepository.save(userValid);
        emailService.sendEmailVerification(userValid, token);

        LOGGER.info("User with {} changed email. Email verification {} pending.",
                userValid.getId(), emailCommand.email());

    }

    public void updateUserEmailVerify(User userValid, Commands.VerificationCommand verificationCommand) {
        LOGGER.info("Verifying user {} with token {} changes email from {}", verificationCommand.userId(), verificationCommand.tokenId(), userValid.getEmail().getEmail());

        userValid.setEmail(new Email(userValid.getAccount().verifyVerificationToken(verificationCommand.tokenId())));
        userRepository.save(userValid);

        LOGGER.info("User {} email change success to {}", userValid.getId(), userValid.getEmail().getEmail());
    }

    public Views.UserView addProfilePicture(User user, Commands.UploadMediaCommand uploadMediaCommand, MultipartFile mediaFile) {
        LOGGER.info("User {} upload profile picture", user.getId());

        user.getPersonal().setProfilPicture(mediaService.saveMedia(mediaFile, uploadMediaCommand));
        return mapper.convert(userRepository.save(user));
    }

    public Views.UserView removeProfilePicture(User user) {
        LOGGER.info("User {} remove profile picture", user.getId());
        mediaService.deleteMedia(user.getPersonal().getProfilPicture().id());
        user.getPersonal().setProfilPicture(null);
        return mapper.convert(userRepository.save(user));
    }

    public Views.NotificationUserView addPartner(User user, Commands.PartnerRequestCommand partnerRequestCommand) {
        LOGGER.info("sending partner request from user with id: " + user.getId() + "to user with email: " + partnerRequestCommand.email());
        User partner = userRepository.findByEmailEmail(partnerRequestCommand.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + partnerRequestCommand.email()));
//        Ensure.sendPartnerRequestValid(user.getId(), partner.getId(), user.getPartner().getPartners());
//        return notificationUserService.createNotificationWithString(user, partner, "Partneranfrage von " + user.getPersonal().getFullName(),1, "FRIEND_REQUEST");
        try {
            return notificationUserService.createNotification(user.getPartner().sendPartnerRequest(partner.getId(), user.getPersonal().getFullName()), user);
        } catch (Exception e) {
            LOGGER.error("Error while sending partner request: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public Views.NotificationUserView deletePartner(User user, String partnerId) {
        LOGGER.info("deleting partner from user with id: " + user.getId() + " and user with id: " + partnerId);
        User partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + partnerId));
        user.getPartner().removePartner(partner.getId());
        partner.getPartner().removePartner(user.getId());
        userRepository.save(user);
        userRepository.save(partner);
        notificationUserService.createNotificationWithString(user, partner, "Partner mit " + user.getPersonal().getFullName() + " wurde gelöscht",0);
        return notificationUserService.createNotificationWithString(partner, user, "Partner mit " + partner.getPersonal().getFullName() + " wurde gelöscht",0);
    }

    //    @Override
//    public Optional<User> updateNewEmail(String id, User user) {
//        User userDB = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
//        userRepository.findAll().stream()
//                .filter(u -> u.getNewEmail().equals(user.getNewEmail()))
//                .findFirst()
//                .ifPresent(u -> {
//                    throw new EmailTakenException("Email already taken");
//                });
//        if(Objects.nonNull(user.getNewEmail()) && !"".equalsIgnoreCase(user.getNewEmail().getEmail())){
//            userDB.setNewEmail(user.getNewEmail().getEmail());
//        }
//        return Optional.of(userRepository.save(userDB));
//    }

//    public Page<User> searchUsers(String firstName, String lastName, String email, String userGroup, Boolean leader, Boolean helper, String completedCourseByType, Pageable pageable) {
//        return userRepository.searchUsers(firstName, lastName, email, userGroup, leader, helper, completedCourseByType, pageable);
//    }

//    public Optional<User> getUserByEmail(Email email) {
//        return userRepository.findByEmail(email);
//    }
//
//
//    public Optional<User> getUserByEmailAndPassword(Email email, Password password) {
//        return userRepository.findByEmailAndPassword(email, password);
//    }
//
//    public Optional<User> getUserByEmailEmailAndPasswordPassword(String email, String password) {
//        return userRepository.findByEmailEmailAndPasswordPassword(email, password);
//    }
//
//    public List<User> getUsersByFirstName(String firstName) {
//        return userRepository.findByPersonalFirstNameIgnoreCase(firstName);
//    }
//
//    public List<User> getUsersByLastName(String lastName) {
//        return userRepository.findByPersonalLastNameIgnoreCase(lastName);
//    }
//
//    public List<User> getUsersByFirstNameAndLastName(String firstName, String lastName) {
//        return userRepository.findByPersonalFirstNameIgnoreCaseAndPersonalLastNameIgnoreCase(firstName, lastName);
//    }
//
//    public List<User> getUsersByUserGroup(User.userGroup userGroup) {
//        return userRepository.findAllByUserGroup(userGroup);
//    }
//
//    public List<User> getUsersByHelperIs(boolean isHelper) {
//        return userRepository.findAllByIsHelperIs(isHelper);
//    }
//
//    public List<User> getUsersByLeaderIs(boolean isLeader) {
//        return userRepository.findAllByIsLeaderIs(isLeader);
//    }
//
//    public List<User> getUsersByHelperIsAndLeaderIs(boolean isHelper, boolean isLeader) {
//        return userRepository.findAllByIsHelperIsAndIsLeaderIs(isHelper, isLeader);
//    }
}


