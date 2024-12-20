import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

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
                //  .then()
                // .assertThat().body(containsString("Job is NOT ready"));
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
        //.then()
        // .assertThat().body(containsString("Job is ready")).body(is("result"));

        String newStatus = responseThird.path("status");
        String result = responseThird.path("result");
        System.out.println(newStatus);
        System.out.println(result);

    }
}


