import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;


import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;


public class LongRedirect {
  @Test
    public void testRestAssured() {
     int count = 0;
   //  int statusCode=0;

       // for (int i=0;i<size;i++) {
      Response  response = RestAssured
                  .given()
                  .redirects()
                 .follow(false)
                  .when()
                  .get("https://playground.learnqa.ru/api/long_redirect")
                  .andReturn();

        // count++;
  //  List<String> jresponse = response.jsonPath().getList("$");
   // for (int i = 0; i < jsonResponse.size(); i++) {


      int statusCode =response.getStatusCode();
      response.print();
     String locationHeader=response.getHeader("Location");
    // System.out.println(locationHeader);

    List<Object> head = new ArrayList<>();
    head.add(statusCode);
if (statusCode !=200) {
  get("https://playground.learnqa.ru/api/long_redirect");
  count++;
}
else {
  System.out.println(locationHeader);
}
}



  /*  while (statusCode !=200) {
      Response  response2= given()
              .redirects()
              .follow(true)
              .when()
              .get("https://playground.learnqa.ru/")
              .andReturn();
      ++count;
    }

    System.out.println(count); */


  }




