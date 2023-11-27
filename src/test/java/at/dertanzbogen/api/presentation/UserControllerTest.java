package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.Fakers.UserFaker;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.util.ApiHelpers;
import at.dertanzbogen.api.service.UserRegistrationService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @LocalServerPort
    private int port;
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserController userController;
    @Autowired
    UserAdminController userAdminController;
    @Autowired
    private UserRegistrationController userRegistrationController;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private LoggerMailSenderImpl mailSender;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final int amountOfUsers = 3;

    UserFixture userFixture;

    private ResponseEntity<String> getResponse(String url, HttpMethod method) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(user.getEmail().getEmail(), UserFaker.DEFAULT_PASSWORD);
        HttpEntity<String> httpEntry = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(url, method, httpEntry, String.class);
    }


    @BeforeEach
    void setUp() {
//        userFixture = UserFixture.toDBFromFaker(amountOfUsers, userRepository);
        userFixture = UserFixture.toDBFromFakerStatic(userRegistrationController, userRepository, mailSender);
        user = userFixture.byRole("ADMIN");
    }


    @Test
    void getUserById() {
        User by = userFixture.by(user -> user.getId().equals(this.user.getId()));
        String url = "http://localhost:" + port + "/api/admin/" + this.user.getId();
        ResponseEntity<String> response = getResponse(url, HttpMethod.GET);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(JsonPath.parse(response.getBody()).read("$.id"), by.getId());
//        assertEquals(by.getId(), user.getId());
    }

    @Test
    void getUserByEmail() {
        User by = userFixture.by(user -> user.getEmail().getEmail().equals("admin.piffel@gmx.at"));
        String url = "http://localhost:" + port + "/api/admin/email/admin.piffel@gmx.at";
        ResponseEntity<String> response = getResponse(url, HttpMethod.GET);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(JsonPath.parse(response.getBody()).read("$.email"), by.getEmail().getEmail());
    }

    @Test
    void deleteUserById() {
        User by = userFixture.by(user -> user.getId().equals(this.user.getId()));
        String url = "http://localhost:" + port + "/api/admin/" + this.user.getId();
        ResponseEntity<String> response = getResponse(url, HttpMethod.DELETE);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("User deleted with id: "+this.user.getId(), response.getBody());
        assertEquals(Optional.empty(),userRepository.findById(by.getId()));
//        assertEquals(JsonPath.parse(response.getBody()).read("$.id"), by.getId());
    }

    @Test
    void updateUser() {

    }
}