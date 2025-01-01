package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

//3.6)BaseCase
public class BaseTestCase {

    //Передается хедер
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();
        //Проверка, что значения хедеров точно пришли
        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name " + name);
        return headers.getValue(name); //если значение есть, то оно возвращается, если нет, то тест падает
    }

    //Передается кукис
    protected String getCookie(Response Response, String name) {
        Map<String, String> cookies = Response.getCookies();
        //Проверка, что значения кукис точно пришли
        assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name " + name);
        return cookies.get(name); //если значение есть, то оно возвращается, если нет, то тест падает
    }

    //Получение данных из JSON
    protected int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }

}