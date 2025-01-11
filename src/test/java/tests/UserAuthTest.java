package tests;


import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import lib.ApiCoreRequests;

//3.5. Функция BeforeEach()
@Epic("USER Cases")
@Story("Authorization User")
@Feature("Autorization")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePosttRequest("https://playground.learnqa.ru/api_dev/user/login", authData);


        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully authorize user by email and password ")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test positive auth user")
    public void testAuthUser() {

        //Проверка, что пользователь точно залогинен
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api_dev/user/auth",
                        this.header,
                        this.cookie);
        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    //4. Негативный тест-кейс на авторизацию.
    // Параметризованный тест. В одном заголовке не будет передаваться куки, а во втором не будет передаваться заголовок.
    @ParameterizedTest
    @Severity(SeverityLevel.BLOCKER)
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition) {

        // УБРАТЬ  RequestSpecification spec = RestAssured.given(); //Создали переменную. Далее пойдёт обогащение запроса
        // spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) { //если пришло только куки, то передается только куки
            //spec.cookie("auth_sid", this.cookie);                                БЫЛО
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie("https://playground.learnqa.ru/api_dev/user/auth",
                    this.cookie);  //СТАЛО
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);


        } else if (condition.equals("headers")) {  //если пришел только хедер, то передается только хедер
            //  spec.header("x-csrf-token", this.header);                     БЫЛО
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken("https://playground.learnqa.ru/api_dev/user/auth",
                    this.header);   //СТАЛО

            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else { //прописаны исключения, вдруг где-то опечатка
            throw new IllegalArgumentException("Condition value is know: " + condition);
        }

        // Удалить Response responseForCheck = spec.get().andReturn();
       // Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }
}
