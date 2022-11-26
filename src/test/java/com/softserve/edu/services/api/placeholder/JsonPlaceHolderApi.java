package com.softserve.edu.services.api.placeholder;

import com.softserve.edu.services.api.common.AbstractWebService;
import com.softserve.edu.services.api.placeholder.endpoints.ClubEndpoint;
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

}
