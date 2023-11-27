package at.dertanzbogen.api.factories;

import at.dertanzbogen.api.domain.main.Address;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;

import java.time.Instant;
import java.util.List;

public class NotificationFactory {

    public static Notification of(User creator, Commands.NotificationCreationCommand notificationCreationCommand) {
        return new Notification.NotificationBuilder()
                .setCreatorId(creator.getId())
                .setSendTo(notificationCreationCommand.sendToId())
                .setAcceptAmount(notificationCreationCommand.acceptAmount())
                .setContent(notificationCreationCommand.content())
                .setNotificationType(notificationCreationCommand.notificationType())
                .build();
    }

    public static Notification of(User creator,User receiver,  String message, int acceptAmount) {
        return new Notification.NotificationBuilder()
                .setCreatorId(creator.getId())
                .setSendTo(List.of(receiver.getId()))
                .setAcceptAmount(acceptAmount)
                .setContent(message)
                .setNotificationType(Notification.NotificationType.INFO.name())
                .build();
    }

    public static Notification of(User creator,User receiver,  String message, int acceptAmount, String notificationType) {
        return new Notification.NotificationBuilder()
                .setCreatorId(creator.getId())
                .setSendTo(List.of(receiver.getId()))
                .setAcceptAmount(acceptAmount)
                .setContent(message)
                .setNotificationType(notificationType)
                .build();
    }

    public static Notification of(User creator, String eventId, Commands.NotificationCreationCommand notificationCreationCommand) {
        return new Notification.NotificationBuilder()
                .setCreatorId(creator.getId())
                .setEventId(eventId)
                .setAcceptAmount(notificationCreationCommand.acceptAmount())
                .setSendTo(notificationCreationCommand.sendToId())
                .setContent(notificationCreationCommand.content())
                .setNotificationType(notificationCreationCommand.notificationType())
                .build();
    }
}
