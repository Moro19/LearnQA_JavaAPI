import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;


public class HelloWorldTest {
    @Test
    public void testRestAssured() {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String jsonResponse = response.getString("messages[1]");
        System.out.println(jsonResponse);
    }
}
