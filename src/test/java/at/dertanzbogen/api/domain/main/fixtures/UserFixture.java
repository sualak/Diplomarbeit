package at.dertanzbogen.api.domain.main.fixtures;

import at.dertanzbogen.api.Fakers.UserFaker;
import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.foundation.JsonUtils;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import at.dertanzbogen.api.presentation.UserRegistrationController;
import at.dertanzbogen.api.util.EmailExtractorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class UserFixture
{
    private final Map<Email, User> users;

    public static final String DEFAULT_PASSWORD = UserFaker.DEFAULT_PASSWORD;
    private static Logger LOGGER = LoggerFactory.getLogger(UserFixture.class);

    public static final String USER_FIXTURE_PATH = "fixtures/user.json";
    // public static final String USER_FIXTURE_PATH = "fixtures/users2.json";


    // ctor -----------------------------------------------------------------

    private UserFixture(List<User> users)
    {
        // Transform List<User> -> Map<String, User>
        this.users = users.stream().collect(
                Collectors.toMap(User::getEmail, Function.identity()));
    }


    // to Memory ---------------------------------------------------------

//    public static UserFixture toMemoryFromJson(String path) throws IOException
//    {
//        List<User> users = JsonHelper.loadJsonListFromClasspath(path, User.class);
//        return new UserFixture(users);
//    }

    public static UserFixture toMemoryFromFaker(int amount)
    {
        return toMemoryFromFaker(amount, DEFAULT_PASSWORD);
    }

    public static UserFixture toMemoryFromFaker(int amount, String password)
    {
        return new UserFixture(UserFaker.generateUsersPassword(password,amount));
    }


    // to DB -------------------------------------------------------------

    public static UserFixture toDBFromJson(
            String path, MongoRepository<User, String> userRepository) throws IOException
    {
        List<User> users = JsonUtils.loadJsonListFromFile(path, User.class);

        userRepository.deleteAll();
        userRepository.saveAll(users);

        return new UserFixture(users);
    }

    public static List<User> toDBFromFaker(
            int amount, MongoRepository<User, String> userRepository)
    {
        List<User> users = UserFaker.generateUsersDefault(amount);

        userRepository.deleteAll();
        return userRepository.saveAll(users);
    }


    public static UserFixture toDBFromFaker(
            int amount, String password, MongoRepository<User, String> userRepository)
    {
        List<User> users = UserFaker.generateUsersPassword(password,amount);

        userRepository.deleteAll();
        userRepository.saveAll(users);

        return new UserFixture(users);
    }

//    public static UserFixture toDBFromFakerStatic(
//            MongoRepository<User, String> userRepository)
//    {
//        List<User> users = UserFaker.generateStaticUsers();
//
//        userRepository.deleteAll();
//        userRepository.saveAll(users);
//
//        return new UserFixture(users);
//    }

//    public static UserFixture toDBFromFakerStatic(
//            UserRegistrationService userRegistrationService, MongoRepository<User, String> userRepository)
//    {
//        List<User> users = UserFaker.generateStaticUsers();
//
//        userRepository.deleteAll();
//        users.forEach(userRegistrationService::register);
//        userRepository.saveAll(users);
//        userRepository.findAll().forEach(user -> userRegistrationService.verifyUser(user.getId(), user.getAccount().getVerificationToken()));
//
//        return new UserFixture(users);
//    }

    public static UserFixture toDBFromFakerStatic(
            UserRegistrationController userRegistrationController,
            MongoRepository<User, String> userRepository,
            LoggerMailSenderImpl loggerMailSender)
    {
        userRepository.deleteAll();
        List<UserRegistrationCommand> users = UserFaker.generateStaticUsersDTO();

        users.forEach(user -> {
            String location = userRegistrationController.register(user).getHeaders().get("Location").get(0);
            String id = location.substring(location.lastIndexOf("/") + 1);;
            userRegistrationController.verify(new Commands.VerificationCommand(id, EmailExtractorHelper.extractTokenId(loggerMailSender)));
        });
//        users.forEach(userRegistrationController::register);
//        userRepository.findAll().forEach(user -> userRegistrationController.verify(user.getId(), user.getAccount().getVerificationToken().toString()));
//        userRepository.findAll().forEach(user -> UserFaker.setBaseDomainField(user, "id", UUID.nameUUIDFromBytes(user.getEmail().getEmail().getBytes()).toString()));

        return new UserFixture(userRepository.findAll());
    }


    // to Json -------------------------------------------------------------

//    public static void toJsonFromFaker(
//            int amount, String path) throws IOException
//    {
//        toJsonFromFaker(amount, DEFAULT_PASSWORD, path);
//    }
//
    public static List<User> toJsonFromFaker(
            int amount, String path) throws IOException
    {
        List<User> users = UserFaker.generateUsers(DEFAULT_PASSWORD, amount);
        users.forEach(user -> user.getAccount().setEnabled(true));
        JsonUtils.writeJsonListToFile(users, path);
        return users;

    }

//    public static List<dummyUser> toJsonFromFakerDummy(
//            int amount, String password, String path) throws IOException
//    {
//        List<dummyUser> users = UserFaker.generateDummyUsers(10);
//        JsonUtils.writeJsonListToFile(users, path);
//        return users;
//
//    }


    // getter -------------------------------------------------------------

    public Map<Email, User> getUsers()
    {
        return users;
    }

    public User byRole(String role)
    {
        return users.values().stream()
                .filter(user -> user.getUserGroup().equals(User.userGroup.valueOf(role)))
                .findFirst()
                .orElseThrow();
    }

    public User by(Predicate<User> predicate)
    {
        return users.values().stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow();
    }


    // Gradle Task -------------------------------------------------------------

    public static void seedDBFromJson(String dbName, String jsonName)
    {
        int exitValue = FixtureDatabaseImporter.fromJson(dbName, "importUsersFromJson", jsonName);

        if (exitValue != 0)
            LOGGER.error("Failed to import users to DB");
        else
            LOGGER.info("Done importing users to DB");
    }

    // from json ---------------------------------------------------------

    public static List<User> fromJson(String path) throws IOException
    {
        return FixtureSerializer.loadJsonListFromFile(path, User.class);
    }


    // Execute Java task from Gradle ---------------------------------------

    public static void main(String[] args) throws IOException
    {
        int amount = parseInt(args[0]);
        String path = args[1];

        toJsonFromFaker(amount, path);

        System.out.println("Done writing " + amount + " users to " + path);
    }
}
