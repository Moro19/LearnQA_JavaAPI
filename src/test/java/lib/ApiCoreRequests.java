package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make GET-request with token and cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request ")
    public Response makeSimpleGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request with auth cookie")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request with token")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make Post-request")
    public Response makePosttRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("CreateUser")
    public JsonPath createUser(Map<String, String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }

    @Step("Login user")
    public Response loginUser(Map<String, String> data) {
        return makePosttRequest("https://playground.learnqa.ru/api/user/login", data);
    }

    @Step("Make PUT-request with token, cookie and body")
    public Response makePutRequest(String url, String token, String cookie, Map<String, String> body) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(body)
                .put(url)
                .andReturn();
    }

    @Step("Make PUT-request without token, cookie")
    public Response makePutRequestWithoutTokenAndCookie(String url, Map<String, String> body) {
        return given()
                .filter(new AllureRestAssured())
                .body(body)
                .put(url)
                .andReturn();
    }

    @Step("Create and login random user")
    public Response createAndLoginRandomUser(Map<String, String> data) {
        createUser(data);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));
        return loginUser(authData);
    }

    @Step("Make DELETE-request with token and cookie")
    public Response deleteRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

}
