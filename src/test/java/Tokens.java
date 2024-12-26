import io.restassured.response.Response;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tokens {
    String baseURI = "https://playground.learnqa.ru/ajax/api";

    @Test
    public void testRestAssured() {

        Response response = given()
                .baseUri(baseURI)
                .when()
                .get("/longtime_job")
                .andReturn();
        String token = response.path("token");
        System.out.println(token);

        Response responseSecond = given()
                .baseUri(baseURI)
                .queryParam("token", token)
                .when()
                .get("/longtime_job")
                .andReturn();

        String jsonResponsestatus = responseSecond.path("status");
        System.out.println(jsonResponsestatus);


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Response responseThird = given()
                .baseUri(baseURI)
                .queryParam("token", token)
                .when()
                .get("/longtime_job");


        String newStatus = responseThird.path("status");
        String result = responseThird.path("result");

        Validate.notNull(result);
        assertEquals("Job is ready", newStatus);

        System.out.println(newStatus);
        System.out.println(result);

    }
}


