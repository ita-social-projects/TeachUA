package com.softserve.edu.services.api.placeholder;

import com.softserve.edu.services.api.common.AbstractWebService;
import com.softserve.edu.services.api.placeholder.endpoints.CategoryEndpoint;
import com.softserve.edu.services.api.placeholder.endpoints.ClubEndpoint;
import com.softserve.edu.services.api.placeholder.endpoints.LoginEndpoint;
import io.restassured.specification.RequestSpecification;

public class JsonPlaceHolderApi extends AbstractWebService {

    private RequestSpecification requestSpecification;

    public JsonPlaceHolderApi() {
        initRequestSpecification();
    }

    protected void initRequestSpecification() {
        requestSpecification = getDefaultSpecification();
    }

    public ClubEndpoint club() {
        return new ClubEndpoint(requestSpecification);
    }

    public CategoryEndpoint category() {
        return new CategoryEndpoint(requestSpecification);
    }

    public LoginEndpoint login() {
        return new LoginEndpoint(requestSpecification);
    }

}
