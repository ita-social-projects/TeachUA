package com.softserve.edu.services.api.common;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;

import static io.restassured.RestAssured.given;

public abstract class AbstractWebEndpoint {

    private final ConfigPropertiesReader config = new ConfigPropertiesReader();
    private final String COOKIE = "Cookie";
    private final String SESSION_ID = "JSESSIONID=";
    protected RequestSpecification specification;

    public AbstractWebEndpoint(RequestSpecification specification) {
        this.specification = specification;
    }

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

    public ValidatableResponse get(RequestSpecification requestSpecification, String path) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword())) // provide credentials
                .when()
                .get(path)                                          // get response on the provided URL
                .then();
    }

    // Overloading
    public ValidatableResponse get(RequestSpecification requestSpecification, String path, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword()))
                .when()
                .get(path, pathParams)                          // get response on the provided URL
                .then();
    }

    public ValidatableResponse post(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if(bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword()))
                .when()
                .post(path, pathParams)                         // post data from body() on the provided URL
                .then();
    }

    public ValidatableResponse put(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if(bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword()))
                .when()
                .put(path, pathParams)                          // put data from body() on the provided URL
                .then();
    }

    public ValidatableResponse patch(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if(bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword()))
                .when()
                .patch(path, pathParams)                        // patch data on the provided URL
                .then();
    }

    public ValidatableResponse delete(RequestSpecification requestSpecification, String path) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword())) // provide credentials
                .when()
                .delete(path)                                   // delete data on the provided URL
                .then();
    }

    // Overloading
    public ValidatableResponse delete(RequestSpecification requestSpecification, String path, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return RestAssured.given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header(COOKIE, SESSION_ID + getSessionID(config.getUserLogin(), config.getUserPassword()))
                .when()
                .delete(path, pathParams)                          // delete data on the provided URL
                .then();
    }

}
