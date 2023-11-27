package at.dertanzbogen.api.domain.main.fixtures;


import at.dertanzbogen.api.config.MongoConfig;
import at.dertanzbogen.api.config.TestFixturesProperties;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.UserRepository;
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
public class UserFixtureTest
{
    @Autowired private UserRepository userRepository;
    @Autowired private TestFixturesProperties fixturesProperties;


    @Test
    public void ensure_serialization_and_deserialization_works() throws IOException
    {
        // Given
        String userFixturesPath = fixturesProperties.getPath() + "/user.json";
        List<User> users = UserFixture.toJsonFromFaker(10, userFixturesPath);

        // When
        List<User> usersSaved = UserFixture.fromJson(userFixturesPath);

        // Then: AssertJ recursive comparison
        assertThat(usersSaved)
                .usingRecursiveComparison()
                .isEqualTo(users);
    }


    @Test
    public void ensure_seedingDBFromFaker_works()
    {
        // Given
        // https://www.mongodb.com/community/forums/t/mongodb-date-has-millisecond-accuracy-however-spring-data-returns-microseconds/135842
        // Instants with microseconds are returned from save() method (@CreatedDate/@LastModifiedDate) but actually milliseconds are saved to DB
        List<User> users = UserFixture.toDBFromFaker(10, userRepository);

        // When
        // Instants with milliseconds are retrieved from the MongoDB collection
        List<User> usersRetrieved = userRepository.findAll();

        // Then: AssertJ recursive comparison
        // Custom comparator for Instant fields, because Spring Data MongoDB returns Instant fields with microsecond accuracy
        assertThat(usersRetrieved)
                .usingRecursiveComparison()
                .withComparatorForType(comparing(Instant::toEpochMilli), Instant.class)
                .isEqualTo(users);
    }
}
