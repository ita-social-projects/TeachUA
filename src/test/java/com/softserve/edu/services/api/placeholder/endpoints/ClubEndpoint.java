package com.softserve.edu.services.api.placeholder.endpoints;

import com.softserve.edu.models.enums.HttpStatus;
import com.softserve.edu.models.placeholder.ClubDto;
import com.softserve.edu.services.api.common.AbstractWebEndpoint;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClubEndpoint extends AbstractWebEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubEndpoint.class);           // logger
    private static final String CLUB_RESOURCE_END = "club/{id}";

    public ClubEndpoint(RequestSpecification specification) {
        super(specification);
    }

    public ClubDto getClubById(String id) {
        return getClubById(id, HttpStatus.OK)
                .extract().as(ClubDto.class);
    }

    public ValidatableResponse getClubById(String id, HttpStatus status) {
        LOGGER.info("Get Club by id [{}]", id);
        return get(
                this.specification,
                CLUB_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }



}
