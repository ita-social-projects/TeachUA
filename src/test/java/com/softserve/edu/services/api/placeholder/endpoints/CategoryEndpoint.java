package com.softserve.edu.services.api.placeholder.endpoints;

import com.softserve.edu.models.enums.HttpStatus;
import com.softserve.edu.models.placeholder.CategoryDto;
import com.softserve.edu.services.api.common.AbstractWebEndpoint;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryEndpoint extends AbstractWebEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubEndpoint.class);           // logger
    private static final String CATEGORY_RESOURCE_END = "category/{id}";

    public CategoryEndpoint(RequestSpecification specification) {
        super(specification);
    }

    public CategoryDto getCategoryById(int id) {
        return getCategoryById(id, HttpStatus.OK)
                .extract().as(CategoryDto.class);
    }

    public ValidatableResponse getCategoryById(int id, HttpStatus status) {
        LOGGER.info("Get Category by id [{}]", id);
        return get(
                this.specification,
                CATEGORY_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }

}
