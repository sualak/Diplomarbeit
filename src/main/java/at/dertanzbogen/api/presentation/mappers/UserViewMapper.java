package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.Personal;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.generic.GenericTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

// MapStruct maps domain objects to presentation objects (views) and vice versa if needed.
// https://mapstruct.org/

// MapStruct is one of the best tools to map domain objects to presentation objects (views) and vice versa if needed.
// It's a compile time tool, so it will not affect the runtime performance.

@Mapper
public interface UserViewMapper extends GenericTypeMapper<User, Views.UserView>
{
    UserViewMapper INSTANCE = Mappers.getMapper(UserViewMapper.class);

    @Mapping(target = "email", source = "email", qualifiedByName = "email")
    @Mapping(target = "isLeader", source = "leader")
    @Mapping(target = "isHelper", source = "helper")
    @Override
    Views.UserView convert(User user);

    @Named("email")
    default String email(Email email)
    {
        return email.getEmail();
    }

    @Named("isLeader")
    default boolean isLeader(boolean isLeader)
    {
        return isLeader;
    }

    @Named("isHelper")
    default boolean isHelper(boolean isHelper)
    {
        return isHelper;
    }
}
