package com.softserve.edu.services.api.placeholder.endpoints;

import com.softserve.edu.models.enums.HttpStatus;
import com.softserve.edu.models.placeholder.login.LoginResponseDto;
import com.softserve.edu.models.placeholder.login.UserLoginDto;
import com.softserve.edu.services.api.common.AbstractWebEndpoint;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class LoginEndpoint extends AbstractWebEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginEndpoint.class);
    private static final String LOGIN_END = "signin";

    // Constructor
    public LoginEndpoint(RequestSpecification specification) {
        super(specification);
    }

    public LoginResponseDto create(UserLoginDto userLoginDto) {
        return create(userLoginDto, HttpStatus.OK)
                .extract().as(LoginResponseDto.class);
    }

    public ValidatableResponse create(UserLoginDto userLoginDto, HttpStatus status) {
        LOGGER.info("Sign in");
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(this.specification);
        if (userLoginDto != null) {
            specBuilder.setBody(userLoginDto);
        }
        return given()
                .spec(specBuilder.build())
                .when()
                .post(LOGIN_END)                         // post data from body() on the provided URL
                .then().statusCode(status.getCode());
    }

}
