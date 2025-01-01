import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CheckUserAgent {


    @ParameterizedTest
    @CsvFileSource (resources = "userAgent.csv", numLinesToSkip = 0, delimiter = ':')
    public void testRestAssured(String userAgent, String platform, String browser, String device) {

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);

        JsonPath response = given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String actualPlatform = response.getString("platform");
        String actualBrowser = response.getString("browser");
        String actualDevice = response.getString("device");

        assertEquals(platform, actualPlatform, "Платформы не совпадают");
        assertEquals(browser, actualBrowser, "Браузеры не совпадают");
        assertEquals(device, actualDevice, "Девайсы не совпадают");
    }
}
