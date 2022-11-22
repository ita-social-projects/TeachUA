package com.softserve.edu.services.api.common;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Base64;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;

public abstract class AbstractWebEndpoint {

    private final ConfigPropertiesReader config = new ConfigPropertiesReader();

    // Encode credentials in Base64
    private String encode(String login, String password) {
        return new String(Base64.getEncoder().encode((login + ":" + password).getBytes()));
    }

    // Get sessionID to be authorized
    private String getSessionID(String login, String password) {
        String authorization = encode(login, password);
        Response response = given()
                .header("Authorization", "Basic " + authorization)  // provide Authorization header
                .when()
                .post(config.getLoginPage())                        // post data into the system
                .then()
                .extract().response();                              // extract received response
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("jSessionId");                          // get jSessionId value
    }

    // Post data into the system
    public void postMethod(LinkedHashMap<String, Object> body, String page) {
        // Provide cookie to be authorized to perform needed actions
        given()
                .relaxedHTTPSValidation()
                .header("Cookie", "JSESSIONID=" + getSessionID(config.getUserLogin(), config.getUserPassword())) // provide Cookie header
                .body(body)                                         // place contact data into body
                .when()                                             // after when() we always write what we want to do
                .post(page);                                        // post data from body() on the provided URL
    }

}
