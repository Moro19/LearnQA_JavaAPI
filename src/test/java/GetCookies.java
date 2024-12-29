import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetCookies {
    @Test
    public void testRestAssured() {
        Response response = RestAssured
                .given()
                .when()
                .get(" https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String cookie = response.getCookie("HomeWork");
        assertEquals("hw_value",cookie,"Что-то странное");
    }
}
