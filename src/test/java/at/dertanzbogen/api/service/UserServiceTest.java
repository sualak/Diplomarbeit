//package at.dertanzbogen.api.service;
//
//import at.dertanzbogen.api.domain.main.User.Email;
//import at.dertanzbogen.api.domain.main.User.User;
//import at.dertanzbogen.api.domain.main.fixtures.UserFixture;
//import at.dertanzbogen.api.email.LoggerMailSenderImpl;
//import at.dertanzbogen.api.persistent.UserRepository;
//import at.dertanzbogen.api.presentation.UserRegistrationController;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserServiceTest {
//
//    public static final String API_USER_REGISTRATION = "/user/registration";
//    public static final String API_USER_VERIFY = "/user/verify";
//
//    public static final String API_USER_UPDATE = "/user";
//
//    private static final String PASSWORD = "DasIstEinStarkes1";
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRegistrationService userRegistrationService;
//    @Autowired
//    private UserRegistrationController userRegistrationController;
//    @Autowired
//    private LoggerMailSenderImpl loggerMailSender;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    UserFixture userFixture;
//
//    private User userT;
//    private User userS;
//    private User userA;
//
//
//    @BeforeEach
//    void setUp() {
//        userFixture = UserFixture.toDBFromFakerStatic(userRegistrationController, userRepository, loggerMailSender);
//        userS = userFixture.byRole("STUDENT");
//        userA = userFixture.byRole("ADMIN");
//        userT = userFixture.byRole("TEACHER");
//    }
//
////    @Test
////    public void saveUserValid()
////    {
////        User userT = new User.UserBuilder()
////                .setFirstName("Test")
////                .setLastName("Test")
////                .setNewEmail("test.piffel@gmx.at")
////                .setPassword("warcraft12")
////                .setHelper(false)
////                .setLeader(false)
////                .setUserGroup(User.userGroup.STUDENT)
////                .build();
////
////        User user = userRegistrationService.register(userT).get();
////        assertEquals(userT, user);
////    }
//
//    @Test
//    public void getAllUsersValid()
//    {
//        assertEquals(3, userService.getAllUsers().size());
//    }
//
//    @Test
//    public void getUserByIdValid()
//    {
//        User user = userService.getUserById(userS.getId()).get();
//        assertEquals(userS, user);
//    }
//    @Test
//    public void deleteUserByIdValid()
//    {
//        userService.deleteUserById(userS.getId());
//        User user = userService.getUserById(userS.getId()).orElse(null);
//        assertNull(user);
//    }
//
//    @Test
//    public void updateUserValid()
//    {
//        User user = userService.getUserById(userS.getId()).get();
//        user.getPersonal().setFirstName("ChrisTest");
//        user.getPersonal().setLastName("PiffelTest");
//        User userUpdate = updateUser(user).getBody().as(User.class);
//        User userCheck = userService.getUserById(userS.getId()).get();
//        assertEquals(userUpdate, userCheck);
//    }
//
//    private Response updateUser(User user)
//    {
//        // rest-assured is a fluent API for testing HTTP endpoints
//        return given()
//                .auth()
//                .preemptive()
//                .basic(user.getEmail().getEmail(), PASSWORD)
//                .when()
//                .put(API_USER_UPDATE)
//                .then()
//                .extract()
//                .response();
//    }
//
//    @Test
//    public void getUserByEmailValid() {
//        User user = userService.getUserByEmail(new Email("student.piffel@gmx.at")).get();
//        assertEquals(userS, user);
//    }
//
//    @Test
//    public void getUserByEmailAndPasswordValid() {
//        User user = userService.getUserByEmailAndPassword(userS.getEmail(), userS.getPassword()).get();
//        assertEquals(user, userS);
//    }
//
//    @Test
//    public void getUsersByFirstNameValid()
//    {
//        User user = userService.getUsersByFirstName(userS.getPersonal().getFirstName()).get(0);
//        assertEquals(userS.getPersonal().getFirstName(), user.getPersonal().getFirstName());
//    }
//
//    @Test
//    public void getUsersByLastNameValid()
//    {
//        User user = userService.getUsersByLastName(userS.getPersonal().getLastName()).get(0);
//        assertEquals(userS.getPersonal().getLastName(), user.getPersonal().getLastName());
//    }
//
//    @Test
//    public void getUsersByFirstNameAndLastNameValid()
//    {
//        User user = userService.getUsersByFirstNameAndLastName(userS.getPersonal().getFirstName(), userS.getPersonal().getLastName()).get(0);
//        assertEquals(userS.getPersonal().getFirstName(), user.getPersonal().getFirstName());
//        assertEquals(userS.getPersonal().getLastName(), user.getPersonal().getLastName());
//    }
//
//    @Test
//    public void getUsersByUserGroup()
//    {
//        assertEquals(1, userService.getUsersByUserGroup(User.userGroup.STUDENT).size());
//    }
//
//    @Test
//    public void getUsersByHelperIsValid()
//    {
//        assertEquals(0, userService.getUsersByHelperIs(true).size());
//    }
//
//    @Test
//    public void getUsersByLeaderIsValid()
//    {
//        assertEquals(3, userService.getUsersByLeaderIs(true).size());
//    }
//
//    @Test
//    public void getUsersByHelperIsAndLeaderIs()
//    {
//        assertEquals(0, userService.getUsersByHelperIsAndLeaderIs(true, true).size());
//        assertEquals(3, userService.getUsersByHelperIsAndLeaderIs(false, true).size());
//        assertEquals(0, userService.getUsersByHelperIsAndLeaderIs(false, false).size());
//    }
//
//
//
//}