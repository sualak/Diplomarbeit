package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Event;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.UserNotFoundException;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public abstract class NotificationAdminViewMapper implements GenericTypeMapper<Notification, Views.NotificationAdminView> {

    UserRepository userRepo;
    UserViewMapper userViewMapper;
    NotificationViewMapper notificationViewMapper;
    User currentUser;

    @Override
    public Views.NotificationAdminView convert(Notification notification) {
        {
            return new Views.NotificationAdminView(userViewMapper.convert(userRepo.findById(notification.getCreatorId()).orElseThrow(UserNotFoundException::new)),
                    notificationViewMapper.convert(notification),
                    userRepo.findAllById(notification.getSendToId()).stream().map(userViewMapper::convert).toList(),
                    userRepo.findAllById(notification.getReadById()).stream().map(userViewMapper::convert).toList(),
                    notification.getReadById().contains(currentUser.getId()),
                    notification.getAcceptedById().contains(currentUser.getId()));
        }
    }
}
