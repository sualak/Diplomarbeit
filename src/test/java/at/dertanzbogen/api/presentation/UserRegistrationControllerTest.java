package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.Personal;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.persistent.UserRepositoryImpl;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import at.dertanzbogen.api.presentation.DTOs.Commands.VerificationCommand;
import at.dertanzbogen.api.presentation.util.ApiHelpers;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static at.dertanzbogen.api.util.EmailExtractorHelper.extractTokenId;
import static at.dertanzbogen.api.util.ResponseExtractorHelper.extractIdFromLocation;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpStatus.*;


class UserRegistrationControllerTest extends AbstractControllerTest {


    private static final String PASSWORD = "DasIstEinStarkes1";


    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private LoggerMailSenderImpl mailSender;

    @Test
    void whenUserRegisters_AndVerifiesAccount_ThenLoginSuccess()
    {
        // 1. User registers
        var command1 = new UserRegistrationCommand(new Personal("chirs", "berni"), "test@gmx.at", PASSWORD,true,false, "ADMIN");
        var response1 = ApiHelpers.registerUser(command1);
        var user = assertUserRegistered(command1, response1);

        // 2. User receives email
        var tokenId = extractTokenId(mailSender);

        // 3. User verifies account (by clicking the link in the email)
        var command2 = new VerificationCommand(user.getId(), tokenId);
        var response2 = ApiHelpers.verifyUser(command2);
        assertUserVerified(command2, response2);

        // 4. User logs in and succeeds
        var response3 = ApiHelpers.loginUser(PASSWORD, user.getEmail().getEmail());
        assertLoginSuccess(response3);
    }


    @Test
    void whenUserRegisters_AndNotVerifiesAccount_ThenLoginFailure()
    {
        // 1. User registers
        var command = new UserRegistrationCommand(new Personal("chris", "berni"), "test2@gmx.at", PASSWORD,true,false, "ADMIN");
        ApiHelpers.registerUser(command);

        // 2. User logs in but fails
        var response = ApiHelpers.loginUser(PASSWORD, command.email());
        assertLoginFailure(response);
    }


    // Assertions Helpers --------------------------------------------------------

    private User assertUserRegistered(UserRegistrationCommand command, Response response)
    {
        String userId = extractIdFromLocation(response);
        User user = userRepository.findById(userId).get();

        assertThat(response.getStatusCode(), equalTo(CREATED.value()));
        assertThat(user.getEmail().getEmail(), equalTo(command.email()));
        assertThat(user.getAccount().isEnabled(), equalTo(false));
        // Ensure the verification token is set
        assertThat(user.getAccount().getVerificationToken(), notNullValue());

        // Return the user for later use
        return user;
    }

    private void assertUserVerified(VerificationCommand command, Response response)
    {
        User user = userRepository.findById(command.userId()).get();

        assertThat(response.getStatusCode(), equalTo(OK.value()));
        assertThat(user.getAccount().isEnabled(), equalTo(true));
        // Ensure the verification token is removed
        assertThat(user.getAccount().getVerificationToken(), nullValue());
    }

    private void assertLoginSuccess(Response response)
    {
        assertThat(response.getStatusCode(), equalTo(OK.value()));
    }

    private void assertLoginFailure(Response response)
    {
        assertThat(response.getStatusCode(), equalTo(UNAUTHORIZED.value()));
    }
}