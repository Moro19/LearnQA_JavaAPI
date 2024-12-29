import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetHeaders {
    @Test
    public void testRestAssured() {
        Response response = RestAssured
                .given()
                .when()
                .get(" https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String hd = response.getHeader("x-secret-homework-header");
        response.print();
        assertEquals("Some secret value" ,hd,"Что-то странное");
    }
}
