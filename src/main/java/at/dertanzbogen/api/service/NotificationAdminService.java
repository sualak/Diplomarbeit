package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.factories.NotificationFactory;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.NotificationRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.NotificationAdminViewMapper;
import at.dertanzbogen.api.presentation.mappers.NotificationViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationAdminService {

    private final NotificationRepository notificationRepository;
    private final CourseAdminService courseAdminService;
    private final CourseRepository courseRepository;
    private final UserAdminService userService;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRepository.class);
    private static final PageMapper mapperPage = new PageMapper() {};

    private static final NotificationViewMapper NOTIFICATION_VIEW_MAPPER = NotificationViewMapper.INSTANCE;
    private static final UserViewMapper USER_VIEW_MAPPER = UserViewMapper.INSTANCE;


    public Notification createNotification(User user, Commands.NotificationCreationCommand notificationCreateCommand, Pageable pageable) {
        return notificationRepository.save(NotificationFactory.of(user, notificationCreateCommand));
    }

    public Views.PageDomainXtoPageDTO<Views.NotificationAdminView> getAllNotifications(User user, Pageable pageable) {
        return mapperPage.convert(notificationRepository.findAllBySendToIdOrCreatorIdOrderBySendAt(user.getId(),user.getId(), pageable), getAdminMapper(user));
    }

    public Views.NotificationAdminView createNotificationByUserQuery(User user, Commands.NotificationCreationCommand notificationCreationCommand, Commands.UserSearchFilterCommand userSearchFilterCommand) {
        List<User> users = userService.searchGenericAll(userSearchFilterCommand);
        notificationCreationCommand.sendToId().clear();
        users.forEach(u -> notificationCreationCommand.sendToId().add(u.getId()));
        return getAdminMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, notificationCreationCommand)));
    }

    public Views.NotificationAdminView createNotificationByCourseQuery(User user, Commands.NotificationCreationCommand notificationCreationCommand, Commands.CourseSearchFilterCommand courseSearchFilterCommand) {
        List<Course> courses = courseAdminService.searchGenericBookedList(courseSearchFilterCommand);
        notificationCreationCommand.sendToId().clear();
        courses.forEach(course -> course.getBooked().forEach(booked -> notificationCreationCommand.sendToId().add(booked.getUserID())));
        return getAdminMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, notificationCreationCommand)));
    }

    public Views.NotificationAdminView createNotificationForEvent(User user, String eventId, Commands.NotificationCreationCommand notificationCreateCommand) {
        return getAdminMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, eventId, notificationCreateCommand)));
    }


    private NotificationAdminViewMapper getAdminMapper(User currentUser) {
        return new NotificationAdminViewMapper(userRepository, USER_VIEW_MAPPER, NOTIFICATION_VIEW_MAPPER, currentUser) {};
    }
}
