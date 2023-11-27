package at.dertanzbogen.api.presentation.util;

import at.dertanzbogen.api.presentation.DTOs.Commands;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiHelpers
{
    public static final String API_USER_REGISTRATION = "/api/user/registration";
    public static final String API_USER_VERIFY = "/api/user/verify";
    public static final String API_USER_LOGIN = "/api/user/login";
    private static final String PASSWORD = "DasIstEinStarkes1";

    public static Response loginUser(String password, String email)
    {
        // rest-assured is a fluent API for testing HTTP endpoints
        return given()
                .auth()
                .preemptive()
                .basic(email, password)
                .when()
                .get(API_USER_LOGIN)
                .then()
                .extract()
                .response();
    }


    public static Response registerUser(Commands.UserRegistrationCommand userRegistrationCommand)
    {
        // rest-assured is a fluent API for testing HTTP endpoints
        return given()
                .contentType(ContentType.JSON)
                .body(userRegistrationCommand)
                .when()
                .post(API_USER_REGISTRATION)
                .then()
                .extract()
                .response();
    }

    public static Response verifyUser(Commands.VerificationCommand verificationCommand)
    {
        // rest-assured is a fluent API for testing HTTP endpoints
        return given()
                .queryParam("userId", verificationCommand.userId())
                .queryParam("tokenId", verificationCommand.tokenId())
                .when()
                .get(API_USER_VERIFY)
                .then()
                .extract()
                .response();
    }


}
