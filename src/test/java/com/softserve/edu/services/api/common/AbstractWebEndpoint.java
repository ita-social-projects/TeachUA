package com.softserve.edu.services.api.common;

import com.softserve.edu.models.placeholder.login.LoginResponseDto;
import com.softserve.edu.models.placeholder.login.UserLoginDto;
import com.softserve.edu.services.api.Services;
import com.softserve.edu.testcases.testdata.TestData;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class AbstractWebEndpoint {
    protected RequestSpecification specification;

    public AbstractWebEndpoint(RequestSpecification specification) {
        this.specification = specification;
    }

    // Get sessionID to be authorized
    private String getAuthorized() {
        UserLoginDto userLoginDto = TestData.loginTestData().signin();
        LoginResponseDto signInResponse = Services.placeHolderApi().login().create(userLoginDto);
        return signInResponse.getAccessToken();
    }

    public ValidatableResponse get(RequestSpecification requestSpecification, String path) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return given()
                .spec(specBuilder.build())
                .when()
                .get(path)                                          // get response on the provided URL
                .then();
    }

    // Overloading
    public ValidatableResponse get(RequestSpecification requestSpecification, String path, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return given()
                .spec(specBuilder.build())
                .when()
                .get(path, pathParams)                          // get response on the provided URL
                .then();
    }

    public ValidatableResponse post(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if (bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header("Authorization", "Bearer " + getAuthorized())
                .when()
                .post(path, pathParams)                         // post data from body() on the provided URL
                .then();
    }

    public ValidatableResponse put(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if (bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header("Authorization", "Bearer " + getAuthorized())
                .when()
                .put(path, pathParams)                          // put data from body() on the provided URL
                .then();
    }

    public ValidatableResponse patch(RequestSpecification requestSpecification, String path, Object bodyPayload, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if (bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return given()
                .spec(specBuilder.build())
                .when()
                .patch(path, pathParams)                        // patch data on the provided URL
                .then();
    }

    public ValidatableResponse delete(RequestSpecification requestSpecification, String path, Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        return given()
                .spec(specBuilder.build())
                // Trust all hosts regardless if the SSL certificate is invalid
                .relaxedHTTPSValidation()
                .header("Authorization", "Bearer " + getAuthorized())
                .when()
                .delete(path, pathParams)                          // delete data on the provided URL
                .then();
    }

}
