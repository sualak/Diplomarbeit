package at.dertanzbogen.api.domain.main.fixtures;

import at.dertanzbogen.api.config.MongoConfig;
import at.dertanzbogen.api.config.TestFixturesProperties;
import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.UserRegistrationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
@Import({ MongoConfig.class, TestFixturesProperties.class })
@DataMongoTest
public class CourseFixtureTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired private TestFixturesProperties fixturesProperties;


    @Test
    public void ensure_serialization_and_deserialization_works() throws IOException
    {
        // Given
        String courseFixturesPath = fixturesProperties.getPath() + "/course.json";
        List<Course> courses = CourseFixture.toJsonFromFaker(5, courseFixturesPath);

        // When
        List<Course> coursesSaved = CourseFixture.fromJson(courseFixturesPath);

        // Then: AssertJ recursive comparison
        assertThat(coursesSaved)
                .usingRecursiveComparison()
                .isEqualTo(courses);
    }


    @Test
    public void ensure_seedingDBFromFaker_works()
    {
        // Given
        // https://www.mongodb.com/community/forums/t/mongodb-date-has-millisecond-accuracy-however-spring-data-returns-microseconds/135842
        // Instants with microseconds are returned from save() method (@CreatedDate/@LastModifiedDate) but actually milliseconds are saved to DB
        List<Course> courses = CourseFixture.toDBFromFaker(10, courseRepository);

        // When
        // Instants with milliseconds are retrieved from the MongoDB collection
        List<Course> coursesRetrieved = courseRepository.findAll();

        // Then: AssertJ recursive comparison
        // Custom comparator for Instant fields, because Spring Data MongoDB returns Instant fields with microsecond accuracy
        assertThat(coursesRetrieved)
                .usingRecursiveComparison()
                .withComparatorForType(comparing(Instant::toEpochMilli), Instant.class)
                .isEqualTo(courses);
    }
}
