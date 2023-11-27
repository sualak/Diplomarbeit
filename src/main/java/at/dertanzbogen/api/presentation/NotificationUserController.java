package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.NotificationAdminViewMapper;
import at.dertanzbogen.api.presentation.mappers.NotificationUserViewMapper;
import at.dertanzbogen.api.presentation.mappers.NotificationViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.NotificationAdminService;
import at.dertanzbogen.api.service.NotificationUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/notification")
@AllArgsConstructor
public class NotificationUserController {

    private final NotificationUserService notificationUserService;
    private final Logger logger = LoggerFactory.getLogger(NotificationUserController.class);
    private static final NotificationViewMapper NOTIFICATION_VIEW_MAPPER = NotificationViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";

    @PostMapping("/create")
    public Views.NotificationUserView createNotification(@AuthenticationUser User user, @RequestBody Commands.NotificationCreationCommand notificationCreateCommand) {
        logger.info("Creating notification");
        return notificationUserService.createNotification(user, notificationCreateCommand);
    }

    @PostMapping("/create/{courseId}")
    public Views.NotificationUserView createNotificationForEvent(@AuthenticationUser User user, @PathVariable String courseId, @RequestBody Commands.NotificationCreationCommand notificationCreateCommand) {
        logger.info("Creating notification for event {}", courseId);
        return notificationUserService.createNotificationForCourse(user, courseId, notificationCreateCommand);
    }

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.NotificationUserView> getAllNotifications(@AuthenticationUser User user,
                                                                                       @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                                       @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        logger.info("Getting page {} from allNotifications", page);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return notificationUserService.getAllMyNotifications(user, pageable);
    }

    @PostMapping("/read/{notificationId}")
    public Views.NotificationUserView readNotification(@AuthenticationUser User user, @PathVariable String notificationId) {
        logger.info("Reading notification");
        return notificationUserService.readNotification(user, notificationId);
    }

    @PostMapping("/accept/{notificationId}")
    public Views.NotificationUserView acceptNotification(@AuthenticationUser User user, @PathVariable String notificationId) {
        logger.info("Accepting notification");
        return notificationUserService.acceptNotification(user, notificationId);
    }

    @PostMapping("/decline/{notificationId}")
    public Views.NotificationUserView declineNotification(@AuthenticationUser User user, @PathVariable String notificationId) {
        logger.info("Declining notification");
        return notificationUserService.declineNotification(user, notificationId);
    }
}
