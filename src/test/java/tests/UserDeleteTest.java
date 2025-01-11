package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("USER Cases")
@Story("DELETE USER")

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    int userId = 2;
    int otherUserId = 3;

    @Test
    @Severity(SeverityLevel.NORMAL)
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
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertJsonHasField(responseDelete, "error");
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Delete user with check")
    public void testDeleteUserSuccess() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        userId = this.getIntFromJson(responseGetAuth, "user_id");

        apiCoreRequests.deleteRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                header, cookie
        );

        Response responseCheckDelete = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseCheckDelete, 404);
        Assertions.assertResponseTextEquals(responseCheckDelete, "User not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Delete user auth other user")
    public void testDeleteOtherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseDeleteOtherUser = apiCoreRequests.deleteRequest(
                "https://playground.learnqa.ru/api_dev/user/" + otherUserId,
                header, cookie
        );

        Assertions.assertResponseCodeEquals(responseDeleteOtherUser, 400);
        Assertions.assertJsonHasField(responseDeleteOtherUser, "error");
    }
}
