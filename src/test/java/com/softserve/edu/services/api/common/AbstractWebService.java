package com.softserve.edu.services.api.common;

import com.softserve.edu.utils.ConfigPropertiesReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class AbstractWebService {

    private ConfigPropertiesReader config = new ConfigPropertiesReader();        // configuration file

    protected AbstractWebService() {
    }

    protected abstract void initRequestSpecification();

    // Set base URL that will be used
    // All methods are static in this class not to create object of that class in test class
    public RequestSpecification getDefaultSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(config.getApiUrl())
                // addFilter we need to use not to write log().all() for each request and response (to write logs by default)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

}
