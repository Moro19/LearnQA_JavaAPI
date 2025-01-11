package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

@Epic("USER Cases")
@Story("Register USER")

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api_dev/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Create user with email without @")
    public void testCreateUserWithNonValidEmail() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = apiCoreRequests.makePosttRequest("https://playground.learnqa.ru/api_dev/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Create user with short firstname (1 symbol)")
    public void testCreateUserWithShortName() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", "l");
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = apiCoreRequests.makePosttRequest("https://playground.learnqa.ru/api_dev/user/",
                userData);


        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Create user with long firstname (more 250)")
    public void testCreateUserWithLongName() {

        String firstName = DataGenerator.getNameWithDetectedLength(251);
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = apiCoreRequests.makePosttRequest("https://playground.learnqa.ru/api_dev/user/",
                userData);


        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @DisplayName("Create user without one field")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithoutField(String key) {
        Map<String, String> userData = DataGenerator.fillDataWithoutOneField(key);

        Response responseCreateAuth = apiCoreRequests.makePosttRequest(
                "https://playground.learnqa.ru/api_dev/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + key);
    }
}