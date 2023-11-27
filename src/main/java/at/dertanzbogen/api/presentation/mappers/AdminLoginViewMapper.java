package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Views;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserViewMapper.class})
public interface AdminLoginViewMapper {

    AdminLoginViewMapper INSTANCE = Mappers.getMapper(AdminLoginViewMapper.class);
    Views.AdminLoginView adminLoginView(User user,
                                        Views.PageDomainXtoPageDTO<Views.NotificationUserView> notifications,
                                        Views.PageDomainXtoPageDTO<Views.UserView> userViewPageX,
                                        Views.PageDomainXtoPageDTO<Views.CourseAdminView> currentOpenCourses);
}
