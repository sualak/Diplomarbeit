package at.dertanzbogen.api.domain.main.fixtures;


import at.dertanzbogen.api.config.MongoConfig;
import at.dertanzbogen.api.config.TestFixturesProperties;
import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.persistent.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static at.dertanzbogen.api.util.CommandLineHelper.executeCommand;
import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;

@Import({ TestFixturesProperties.class, MongoConfig.class })
@DataMongoTest
public class SeedDatabaseTest
{
    @Autowired private MongoProperties mongoProperties;
    @Autowired private TestFixturesProperties fixturesProperties;

    @Autowired private UserRepository userRepository;


//    @Test
//    void ensure_seedingDatabase_works() throws IOException
//    {
//        // Given
//        String userFixturesPath = fixturesProperties.getPath() + "/user.json";
////        String productFixturesPath = fixturesProperties.getPath() + "/product.json";
//
//        int exitValue = seedDatabase(mongoProperties.getDatabase(), fixturesProperties.getPath());
//        List<User> users = UserFixture.fromJson(userFixturesPath);
//
//        // When
//        List<User> usersRetrieved = userRepository.findAll();
//
//        // Then
//        assertThat(exitValue).isEqualTo(0);
//        assertRetrieved(usersRetrieved, users);
//    }


    private <T> void assertRetrieved(List<T> retrieved, List<T> expected)
    {
        // AssertJ recursive comparison
        // - Ignoring version field, because after saving to DB the version went from NULL to 0
        // - Ignoring collection order, because the order of the products in the DB is not guaranteed
        // - Custom comparator for Instant fields needed because of the difference in precision
        assertThat(retrieved)
            .usingRecursiveComparison()
            .ignoringFields("version")
            .ignoringCollectionOrder()
            .withComparatorForType(comparing(Instant::toEpochMilli), Instant.class)
            .isEqualTo(expected);
    }


    public int seedDatabase(String dbName, String fixturePath)
    {
       return executeCommand("./tools/db/seed-database.sh",dbName, fixturePath, "10");
    }

}
