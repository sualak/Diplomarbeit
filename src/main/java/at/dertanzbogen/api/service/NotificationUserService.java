package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.CourseNotFoundException;
import at.dertanzbogen.api.domain.main.error.NotificationNotFoundException;
import at.dertanzbogen.api.factories.NotificationFactory;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.NotificationRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.NotificationUserViewMapper;
import at.dertanzbogen.api.presentation.mappers.NotificationViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class NotificationUserService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRepository.class);
    private static final PageMapper mapperPage = new PageMapper() {};

    private static final NotificationViewMapper NOTIFICATION_VIEW_MAPPER = NotificationViewMapper.INSTANCE;
    private static final UserViewMapper USER_VIEW_MAPPER = UserViewMapper.INSTANCE;

    public Views.NotificationUserView createNotification(User user, Commands.NotificationCreationCommand notificationCreateCommand) {
        notificationCreateCommand.sendToId().clear();
        userRepository.findAllByUserGroup(User.userGroup.ADMIN).forEach(admin -> notificationCreateCommand.sendToId().add(admin.getId()));
        return getUserMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, notificationCreateCommand)));
    }

    public Views.NotificationUserView createNotification (Notification notification, User currentUser) {
        return getUserMapper(currentUser).convert(notificationRepository.save(notification));
    }

    public Views.NotificationUserView createNotificationWithString(User user, User receiver, String message, int acceptAmount) {
        return getUserMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, receiver, message, acceptAmount)));
    }

    public Views.NotificationUserView createNotificationWithString(User user, User receiver, String message, int acceptAmount, String notificationType) {
        return getUserMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, receiver, message, acceptAmount, notificationType)));
    }

    public Views.PageDomainXtoPageDTO<Views.NotificationUserView> getAllMyNotifications(User user, Pageable pageable) {
        return mapperPage.convert(notificationRepository
                .findAllBySendToIdAndAcceptedByIdIsNotContainingOrderBySendAt(user.getId(), user.getId(), pageable), getUserMapper(user));
    }

    public Views.NotificationUserView readNotification(User user, String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

            if (notification.getSendToId().contains(user.getId())) {
                notification.addToReadBy(user.getId());
                notificationRepository.save(notification);
            }

        return getUserMapper(user).convert(notification);
    }

    public Views.NotificationUserView acceptNotification(User user, String notificationId) {
        LOGGER.info("User {} accepts Notification {}", user.getId(), notificationId);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        if (notification.getSendToId().contains(user.getId())) {
            notification.addToAcceptedBy(user.getId());
        }

        switch (notification.getNotificationType()) {
            case HELPER_REQUEST:
                AtomicReference<String> date = new AtomicReference<>("");
                Course course = courseRepository.findCourseByEventsIdAndCourseStatus(notification.getEventId(), CourseBaseEntity.CourseStatus.OPEN).orElseThrow(() -> new CourseNotFoundException("No Course with EventID "+ notification.getEventId() +" not found"));
                        course.getEvents().stream().filter(event -> event.getId().equals(notification.getEventId())).forEach(event -> {
                                    event.addBookedForChange(user, user.isLeader());
                                    date.set(event.getStartsAt().atZone(ZoneId.of("Europe/Paris")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                        });
                    courseRepository.save(course);
                    createNotificationWithString(user, user, "Du wurdest als Helfer eingetragen in dem event am "+ date, 0, "HELPER_ACCEPTED");
                break;
            case FRIEND_REQUEST:
                user.getPartner().addPartner(notification.getCreatorId());
                User transmitter = userRepository.findById(notification.getCreatorId()).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
                transmitter.getPartner().addPartner(user.getId());
                userRepository.save(user);
                userRepository.save(transmitter);
                break;
        }

        notificationRepository.save(notification);

        return getUserMapper(user).convert(notification);
    }

    public Views.NotificationUserView createNotificationForCourse(User user, String courseId, Commands.NotificationCreationCommand notificationCreateCommand) {
        return getUserMapper(user).convert(notificationRepository.save(NotificationFactory.of(user, courseId, notificationCreateCommand)));
    }

    private NotificationUserViewMapper getUserMapper(User currentUser) {
        return new NotificationUserViewMapper(NOTIFICATION_VIEW_MAPPER, currentUser) {};
    }

    public Views.NotificationUserView declineNotification(User user, String notificationId) {
        LOGGER.info("User {} declines Notification {}", user.getId(), notificationId);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        notification.removeFromSendTo(user.getId());

        notificationRepository.save(notification);

        return getUserMapper(user).convert(notification);
    }
}
