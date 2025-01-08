package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Get user data not authorized")
    public void testGetUserDataNotAuth() {
        Response responseUserData = apiCoreRequests.makeSimpleGetRequest
                ("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonNotHasField(responseUserData, "firstName");
        Assertions.assertJsonNotHasField(responseUserData, "lastName");
        Assertions.assertJsonNotHasField(responseUserData, "email");
    }

    @Test
    @DisplayName("Get user data as same user")
    public void testGetUserDetailAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests.makePosttRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/2",
                header,
                cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @DisplayName("Get user data authorized not as same user")
    public void testGetUserDetailAuthNotAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests.makePosttRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/1",
                header,
                cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonNotHasField(responseUserData, "firstName");
        Assertions.assertJsonNotHasField(responseUserData, "lastName");
        Assertions.assertJsonNotHasField(responseUserData, "email");

    }
}