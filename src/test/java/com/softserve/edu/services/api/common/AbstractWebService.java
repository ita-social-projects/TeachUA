package com.softserve.edu.services.api.common;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class AbstractWebService {

    private static ConfigPropertiesReader config = new ConfigPropertiesReader();        // configuration file
    private static final int okStatusCode = 200;                                        // OK status code
    private static final int errorStatusCode = 400;                                     // Bad request status code

    // Set base URL that will be used
    // All methods are static in this class not to create object of that class in test class
    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(config.getApiUrl())
                .setContentType(ContentType.JSON)
                .build();
    }

    // Specification to check if we get status code 200 OK
    public static ResponseSpecification responseSpecificationOK() {
        return new ResponseSpecBuilder()
                .expectStatusCode(okStatusCode)
                .build();
    }

    // Specification to check if we get status code 400 Error
    public static ResponseSpecification responseSpecificationError() {
        return new ResponseSpecBuilder()
                .expectStatusCode(errorStatusCode)
                .build();
    }

    // Specification to check if we get custom status code
    public static ResponseSpecification responseSpecificationOther(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }

    // Set base url and check if we received expected response status
    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }

    // Set base URL
    public static void installRequestSpecification(RequestSpecification request) {
        RestAssured.requestSpecification = request;
    }

    // Check if we received expected response status
    public static void installResponseSpecification(ResponseSpecification response) {
        RestAssured.responseSpecification = response;
    }

}
