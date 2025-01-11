package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    int userId = 2;
    int otherUserId = 3;

    @Test
    @DisplayName("Delete user by id")

    public void testDeleteUserFail() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.loginUser(authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        userId = this.getIntFromJson(responseGetAuth, "user_id");

        Response responseDelete = apiCoreRequests.deleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertJsonHasField(responseDelete, "error");
    }


    @Test
    @DisplayName("Delete user with check")
    public void testDeleteUserSuccess() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        userId = this.getIntFromJson(responseGetAuth, "user_id");

        apiCoreRequests.deleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Response responseCheckDelete = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseCheckDelete, 404);
        Assertions.assertResponseTextEquals(responseCheckDelete, "User not found");
    }

    @Test
    @DisplayName("Delete user auth other user")
    public void testDeleteOtherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseDeleteOtherUser = apiCoreRequests.deleteRequest(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDeleteOtherUser, 400);
        Assertions.assertJsonHasField(responseDeleteOtherUser, "error");
    }
}
