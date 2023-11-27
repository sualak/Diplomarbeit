package at.dertanzbogen.api.presentation.mappers;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.UserRepository;
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
class PageXUserViewMapperTest {
    @LocalServerPort
    private int port;
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegistrationController userRegistrationController;
    @Autowired
    private UserAdminController userAdminController;
    @Autowired
    private LoggerMailSenderImpl mailSender;
    @Autowired
    private UserAdminService userAdminService;
    private static final UserViewMapper mapper = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};
    UserFixture userFixture;

    @Test
    void userPageToUserViewPage() {
        // Given
        userFixture = UserFixture.toDBFromFakerStatic(userRegistrationController, userRepository, mailSender);
        user = userFixture.byRole("ADMIN");
        int page = 0;
        Pageable pageable = Pageable.ofSize(10);
        // When
        Views.PageDomainXtoPageDTO<Views.UserView> allUsers = mapperPage.convert(userAdminService.getAllUsers(pageable), mapper);

        // Then
        assertThat(allUsers, notNullValue());
        assertThat(allUsers.pageNumber(), equalTo(page));
        assertThat(allUsers.totalPages(), equalTo(page+1));
        assertThat(allUsers.totalElements(), equalTo(userRepository.count()));
        assertThat(allUsers.content().get(0).email(), equalTo(user.getEmail().getEmail()));
    }
}