package at.dertanzbogen.api.Fakers;

import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Tables.CompletedCourse;
import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.Personal;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public abstract class UserFaker {

    public static final String DEFAULT_PASSWORD = "DasIstEinStarkesPasswort!";

    // https://code-held.com/2021/05/15/spring-password-encoder/
    private static final PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static List<User> generateUsersDefault(int amount) {
        return generateUsers(DEFAULT_PASSWORD, amount);
    }

    public static List<User> generateUsersPassword(String password, int amount) {
        return generateUsers(password, amount);
    }

    public static List<UserRegistrationCommand>  generateUsersDTO(int amount) {
        Faker userFaker = new Faker(new Locale("de-AT"), new Random(10000));

        List<UserRegistrationCommand> users = Stream.generate(() -> {
            String email = userFaker.internet().emailAddress();

            return new UserRegistrationCommand(
                    new Personal(userFaker.name().firstName(),
                            userFaker.name().lastName()),
                    email,
                    DEFAULT_PASSWORD,
                    userFaker.bool().bool(),
                    userFaker.bool().bool(),
                    "STUDENT"
            );
        }).limit(amount).toList();

        return users;
    }

//    public static List<dummyUser> generateDummyUsers(int amount)
//    {
//        Faker userFaker = new Faker(new Locale("de-AT"), new Random(10000));
//
//        return Stream.generate(() -> {
//            String email = userFaker.internet().emailAddress();
//
//            return new dummyUser.dummyUserBuilder().setEmail(new Email(email)).build();
//        }).limit(amount).toList();
//    }

    public static List<User>  generateUsers(String password, int amount) {
        Faker userFaker = new Faker(new Locale("de-AT"), new Random(10000));

        return Stream.generate(() -> {
            String email = userFaker.internet().emailAddress();

            return new User.UserBuilder()
                    .setFirstName(userFaker.name().firstName())
                    .setLastName(userFaker.name().lastName())
                    .setEmail(email)
                    .setPassword(passwordEncoder.encode(password))
                    .setHelper(userFaker.bool().bool())
                    .setLeader(userFaker.bool().bool())
                    .setUserGroup(User.userGroup.STUDENT)
                    .setId(UUID.nameUUIDFromBytes(email.getBytes()).toString())
                    .build();
        }).limit(amount).toList();
    }


    public static List<UserRegistrationCommand> generateStaticUsersDTO()
    {
        List<UserRegistrationCommand> users = new ArrayList<>();

        UserRegistrationCommand userB = new UserRegistrationCommand(
                new Personal("stella", "piffel"),
                "admin.piffel@gmx.at",
                DEFAULT_PASSWORD,
                true,
                false,
                "ADMIN"
        );


        UserRegistrationCommand userT = new UserRegistrationCommand(
                new Personal("rahel", "piffel"),
                "teacher.piffel@gmx.at",
                DEFAULT_PASSWORD,
                true,
                false,
                "TEACHER"
        );

        UserRegistrationCommand userS = new UserRegistrationCommand(
                new Personal("chris", "piffel"),
                "student.piffel@gmx.at",
                DEFAULT_PASSWORD,
                true,
                false,
                "STUDENT"
        );

        users.add(userB);
        users.add(userT);
        users.add(userS);
        return users;
    }

    public static List<User> generateStaticUsers()
    {
        List<User> users = new ArrayList<>();

        User userB = new User.UserBuilder()
                .setFirstName("stella")
                .setLastName("piffel")
                .setEmail("admin.piffel@gmx.at")
                .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.ADMIN)
                .setId(UUID.nameUUIDFromBytes("admin.piffel@gmx.at".getBytes()).toString())
                .build();


        User userC = new User.UserBuilder()
                .setFirstName("chris")
                .setLastName("piffel")
                .setEmail("chris.piffel@gmx.at")
                .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.ADMIN)
                .setId(UUID.nameUUIDFromBytes("chris.piffel@gmx.at".getBytes()).toString())
                .build();

        User userT = new User.UserBuilder()
                .setFirstName("rahel")
                .setLastName("piffel")
                .setEmail("teacher.piffel@gmx.at")
                .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.TEACHER)
                .setId(UUID.nameUUIDFromBytes("teacher.piffel@gmx.at".getBytes()).toString())
                .build();

        User userS = new User.UserBuilder()
                .setFirstName("chris")
                .setLastName("piffel")
                .setEmail("student.piffel@gmx.at")
                .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                .setHelper(true)
                .setLeader(true)
                .setUserGroup(User.userGroup.STUDENT)
                .setId(UUID.nameUUIDFromBytes("student.piffel@gmx.at".getBytes()).toString())
                .build();

        CompletedCourse completedCourse = new CompletedCourse(true,"test", "1", userB.getId(), Course.CourseType.LEVEL1);

        userB.addCompletedCourse(completedCourse);

        users.add(userB);
        users.add(userC);
        users.add(userT);
        users.add(userS);
        return users;
    }

    public static <T extends BaseEntity> void setBaseDomainField(T entity, String field, Object value)
    {
        try {
            // Get the class of the entity
            var clazz = entity.getClass();

            // Get the field of the BaseDomain class
            Field declaredField = clazz.getSuperclass().getDeclaredField(field);

            // Set the field to be accessible and set the value
            declaredField.setAccessible(true);
            declaredField.set(entity, value);

        } catch (Throwable e) {
            // We should never get here
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
