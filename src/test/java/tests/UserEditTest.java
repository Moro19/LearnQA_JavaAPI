package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.DataGenerator;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String otherUserId = "2";

    @Test
    @DisplayName("Edit user")
    public void testEditJustCreatedUser() {
//GENERATE
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUser(userData);
        String userId = responseCreateAuth.getString("id");
//LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.loginUser(authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
//EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);
//GET
        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @DisplayName("Edit user without auth")
    public void testEditUserNotAuthorized() {
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("lastName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithoutTokenAndCookie(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonHasField(responseEditUser, "error");
    }


    @Test
    @DisplayName("Edit user auth other user")
    public void testEditUserAuthorizedOther() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUser(userData);
        String userId = responseCreateAuth.getString("id");
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + otherUserId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonHasField(responseEditUser, "error");
    }

    @Test
    @DisplayName("Edit user not valid email")
    public void testEditUserEmail() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUser(userData);
        String userId = responseCreateAuth.getString("id");
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newEmail = "yandexyandex.ru";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonHasField(responseEditUser, "error");
    }

    @Test
    @DisplayName("Edit user with short firstname (1 symbol)")
    public void testEditUserFirstName() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.createUser(userData);
        String userId = responseCreateAuth.getString("id");
        Response responseGetAuth = apiCoreRequests.createAndLoginRandomUser(userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        String newFirstName = "L";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                header, cookie,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonHasField(responseEditUser, "error");
    }
}







