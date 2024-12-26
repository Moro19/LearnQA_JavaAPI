import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class LongRedirect {
    @Test
    public void testLongRedirect() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int counter = 0;
        int statusCode = 0;

        while (statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            counter++;


            statusCode = response.getStatusCode();
            url = response.getHeader("Location");
        }
        System.out.println("Количество редиректов: " + counter);
    }

}







