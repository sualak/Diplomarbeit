package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Views;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


// MapStruct maps domain objects to presentation objects (views) and vice versa if needed.
// https://mapstruct.org/

// MapStruct is one of the best tools to map domain objects to presentation objects (views) and vice versa if needed.
// It's a compile time tool, so it will not affect the runtime performance.

// We can use multiple mappers in one mapper
@Mapper(uses = {UserViewMapper.class, PartnerViewMapper.class})
public interface UserLoginViewMapper
{
    UserLoginViewMapper INSTANCE =
            Mappers.getMapper(UserLoginViewMapper.class);

    Views.UserLoginView convert(User user,
                                List<User> partners,
                                Views.PageDomainXtoPageDTO<Views.NotificationUserView> notifications,
                                Views.PageDomainXtoPageDTO<Views.CourseUserView> currentOpenCourses);
}

