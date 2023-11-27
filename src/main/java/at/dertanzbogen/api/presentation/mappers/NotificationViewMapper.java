package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.Course.Booked;
import at.dertanzbogen.api.domain.main.Course.Event;
import at.dertanzbogen.api.domain.main.Notification;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
@Mapper
public interface NotificationViewMapper extends GenericTypeMapper<Notification, Views.NotificationView> {

    NotificationViewMapper INSTANCE = Mappers.getMapper(NotificationViewMapper.class);

    // If the source and target field names are the same, we can omit the @Mapping annotation
    // @Mapping(target = "roles", source = "userRoles")

    // If the source and the target differ in type, we can use a qualifiedByName
    @Mapping(target = "acceptedBy", source = "acceptedById", qualifiedByName = "acceptedBy")
    @Override
    Views.NotificationView convert(Notification notification);

    @Named("acceptedBy")
    default int isPairCourse(Set<String> acceptedById) {
        return acceptedById.size();
    }
}
