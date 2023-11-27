package at.dertanzbogen.api.domain.main.fixtures;

import at.dertanzbogen.api.Fakers.UserFaker;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.Email;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
import at.dertanzbogen.api.foundation.JsonUtils;
import at.dertanzbogen.api.presentation.DTOs.Commands;
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

public class CourseFixture {

    private static Logger LOGGER = LoggerFactory.getLogger(CourseFixture.class);

    public static final String USER_FIXTURE_PATH_Courses = "fixtures/course.json";



    // to DB -------------------------------------------------------------

    public static List<Course> toDBFromFaker(
            int amount, MongoRepository<Course, String> courseRepository)
    {
        List<Course> users = CourseFaker.generateCoursesOfAllTypes(amount);

        courseRepository.deleteAll();
        return courseRepository.saveAll(users);
    }

    public static void toDBFromJson(
            String path, MongoRepository<Course, String> courseRepository) throws IOException
    {
        List<Course> courses = JsonUtils.loadJsonListFromFile(path, Course.class);

        courseRepository.deleteAll();
        courseRepository.saveAll(courses);

    }



    public static List<Course> toJsonFromFaker(
            int amount, String path) throws IOException
    {
        List<Course> courses = CourseFaker.generateCoursesOfAllTypes(amount);
        JsonUtils.writeJsonListToFile(courses, path);
        return courses;

    }


    // Gradle Task -------------------------------------------------------------

    public static void seedDBFromJson(String dbName, String jsonName)
    {
        int exitValue = FixtureDatabaseImporter.fromJson(dbName, "importCoursesFromJson", jsonName);

        if (exitValue != 0)
            LOGGER.error("Failed to import users to DB");
        else
            LOGGER.info("Done importing users to DB");
    }

    // from json ---------------------------------------------------------

    public static List<Course> fromJson(String path) throws IOException
    {
        return FixtureSerializer.loadJsonListFromFile(path, Course.class);
    }


    // Execute Java task from Gradle ---------------------------------------

    public static void main(String[] args) throws IOException
    {
        int amount = parseInt(args[0]);
        String path = args[1];

        toJsonFromFaker(amount, path);

        System.out.println("Done writing " + amount + " courses to " + path);
    }
}
