package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.fixtures.CourseFixture;
import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.CourseAdminController;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.UserAdminController;
import at.dertanzbogen.api.presentation.UserRegistrationController;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminLoginViewMapperTest {
    @LocalServerPort
    private int port;
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRegistrationController userRegistrationController;
    @Autowired
    private UserAdminController userAdminController;
    @Autowired
    private LoggerMailSenderImpl mailSender;
    @Autowired
    private CourseAdminController courseAdminController;
    @Autowired
    private UserAdminService userAdminService;
    private static final UserViewMapper mapper = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};
    UserFixture userFixture;

    @Test
    void adminLoginView() {
        // Given
        userFixture = UserFixture.toDBFromFakerStatic(userRegistrationController, userRepository, mailSender);
        user = userFixture.byRole("ADMIN");
        CourseFixture.toDBFromFaker(10, courseRepository);
        int page = 0;
        Pageable pageable = Pageable.ofSize(10);
        Views.PageDomainXtoPageDTO<Views.UserView> allUsers = mapperPage.convert(userAdminService.getAllUsers(pageable), mapper);
        Views.PageDomainXtoPageDTO<Views.CourseAdminView> openCourses = courseAdminController.getAllCoursesByStatus("OPEN", 0, 10);
        // When
        Views.AdminLoginView adminLoginView = AdminLoginViewMapper.INSTANCE.adminLoginView(user,null, allUsers, openCourses);

        // Then
        assertThat(adminLoginView, notNullValue());
        assertThat(adminLoginView.user().email(), equalTo(user.getEmail().getEmail()));
        assertThat(adminLoginView.userViewPageX().pageNumber(), equalTo(allUsers.pageNumber()));
        assertThat(adminLoginView.userViewPageX().totalPages(), equalTo(allUsers.totalPages()));
        assertThat(adminLoginView.userViewPageX().totalElements(), equalTo(allUsers.totalElements()));
        assertThat(adminLoginView.userViewPageX().content().get(0).email(), equalTo(allUsers.content().get(0).email()));
        assertThat(adminLoginView.currentOpenCourses().content().get(0).courseView().id(), equalTo(openCourses.content().get(0).courseView().id()));
        assertThat(adminLoginView.currentOpenCourses().content().get(0).events().get(0).eventView().id(), equalTo(openCourses.content().get(0).events().get(0).eventView().id()));
    }
}