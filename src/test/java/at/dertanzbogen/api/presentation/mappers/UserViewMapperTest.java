package at.dertanzbogen.api.presentation.mappers;


import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.UserRegistrationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserViewMapperTest
{
    @LocalServerPort
    private int port;
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegistrationController userRegistrationController;
    @Autowired
    private LoggerMailSenderImpl mailSender;
    UserFixture userFixture;

    @Test
    void ensure_userView_works()
    {
        // Given
        userFixture = UserFixture.toDBFromFakerStatic(userRegistrationController, userRepository, mailSender);
        user = userFixture.byRole("ADMIN");

        // When
        Views.UserView userView = UserViewMapper.INSTANCE.convert(user);

        // Then
        assertThat(userView, notNullValue());
        assertThat(userView.email(), equalTo(user.getEmail().getEmail()));
        assertThat(userView.userGroup(), equalTo(user.getUserGroup().name()));
    }
}