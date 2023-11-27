package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.*;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.CourseAdminService;
import at.dertanzbogen.api.service.CourseUserService;
import at.dertanzbogen.api.service.NotificationAdminService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notification")
@AllArgsConstructor
public class NotificationAdminController {

    private final UserRepository userRepository;

    private final NotificationAdminService notificationAdminService;
    private final Logger logger = LoggerFactory.getLogger(NotificationAdminController.class);
    private static final NotificationViewMapper NOTIFICATION_VIEW_MAPPER = NotificationViewMapper.INSTANCE;
    private static final UserViewMapper USER_VIEW_MAPPER = UserViewMapper.INSTANCE;

    private static final PageMapper mapperPage = new PageMapper() {};
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";

    @PostMapping("/create")
    public Views.NotificationAdminView createNotification(@AuthenticationUser User user, @RequestBody Commands.NotificationCreationCommand notificationCreateCommand,
                                                          @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                          @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        logger.info("Creating notification");
        Pageable pageable = PageRequest.of(page, size);
        return getAdminMapper(user).convert(notificationAdminService.createNotification(user, notificationCreateCommand, pageable));
    }

    @PostMapping("/createByUserQuery")
    public Views.NotificationAdminView createNotificationByUserQuery(@AuthenticationUser User user, @RequestBody Commands.NotificationCreationCommand notificationCreationCommand, @RequestBody Commands.UserSearchFilterCommand userSearchFilterCommand){
        logger.info("Create Notification for UsersSearching users with firstName: " + userSearchFilterCommand.firstName() +
                " and lastName: " + userSearchFilterCommand.lastName() +
                " and email: " + userSearchFilterCommand.email() +
                " and userGroup: " + userSearchFilterCommand.userGroup() +
                " and leader: " + userSearchFilterCommand.isLeader() +
                " and helper: " + userSearchFilterCommand.isHelper() +
                " and completedCourseByType: " + userSearchFilterCommand.completedCourseByType());

        return notificationAdminService.createNotificationByUserQuery(user,notificationCreationCommand, userSearchFilterCommand);
    }

    @PostMapping("/createByCourseQuery")
    public Views.NotificationAdminView createNotificationByCourseQuery(@AuthenticationUser User user, @RequestBody Commands.NotificationCreationCommand notificationCreationCommand, @RequestBody Commands.CourseSearchFilterCommand courseSearchFilterCommand){


        return notificationAdminService.createNotificationByCourseQuery(user,notificationCreationCommand, courseSearchFilterCommand);
    }

    @GetMapping()
    public Views.PageDomainXtoPageDTO<Views.NotificationAdminView> getAllNotifications(@AuthenticationUser User user,
                                                                                        @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                                        @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        logger.info("Getting page {} from allNotifications", page);

        Pageable pageable = PageRequest.of(page, size);

        return notificationAdminService.getAllNotifications(user, pageable);
    }

    @PostMapping("/create/{eventId}")
    public Views.NotificationAdminView createNotificationForEvent(@AuthenticationUser User user, @PathVariable String eventId, @RequestBody Commands.NotificationCreationCommand notificationCreateCommand) {
        logger.info("Creating notification for event {}", eventId);
        return notificationAdminService.createNotificationForEvent(user, eventId, notificationCreateCommand);
    }

    private NotificationAdminViewMapper getAdminMapper(User currentUser) {
        return new NotificationAdminViewMapper(userRepository, USER_VIEW_MAPPER, NOTIFICATION_VIEW_MAPPER, currentUser) {};
    }
}
