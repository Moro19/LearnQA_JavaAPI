package lib;

import java.util.HashMap;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.github.javafaker.Faker;


public class DataGenerator {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }

    public static String getRandomValidEmailWithFaker() {
        Faker faker = new Faker(new Locale("en-GB"));
        return faker.internet().emailAddress();
    }

    public static Map<String, String> getRegistrationData() {

        Map<String, String> data = new HashMap<>();
        data.put("email", getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return data;
    }


    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static String getNameWithDetectedLength(int length) {
        Faker faker = new Faker(new Locale("en-GB"));
        return faker.lorem().fixedString(length);
    }
    public static Map<String, String> fillDataWithoutOneField(String field) {
        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (field.equals(key)) {
                userData.put(key, null);
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}

