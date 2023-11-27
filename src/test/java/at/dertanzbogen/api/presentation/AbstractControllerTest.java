package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.persistent.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractControllerTest
{
    @LocalServerPort
    protected int port;

    @Autowired
    protected UserRepository userRepository;

    @BeforeAll
    void setUp()
    {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        userRepository.deleteAll();
    }

//     @Autowired
//     protected TestRestTemplate restTemplate;
}
