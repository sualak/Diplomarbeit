package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.UserNotFoundException;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public abstract class NotificationUserViewMapper implements GenericTypeMapper<Notification, Views.NotificationUserView> {
    NotificationViewMapper notificationViewMapper;
    User currentUser;

    @Override
    public Views.NotificationUserView convert(Notification notification) {
        {
            return new Views.NotificationUserView(notificationViewMapper.convert(notification),
                    notification.getReadById().contains(currentUser.getId()),
                    notification.getAcceptedById().contains(currentUser.getId()));
        }
    }
}
