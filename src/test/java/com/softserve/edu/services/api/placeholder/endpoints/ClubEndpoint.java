package com.softserve.edu.services.api.placeholder.endpoints;

import com.softserve.edu.models.enums.HttpStatus;
import com.softserve.edu.models.placeholder.ClubResponseDto;
import com.softserve.edu.services.api.common.AbstractWebEndpoint;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClubEndpoint extends AbstractWebEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubEndpoint.class);           // logger
    private static final String CLUBS_END = "clubs";
    private static final String CLUB_RESOURCE_END = "club/{id}";
    private static final String CLUBS_BY_CENTER_RESOURCE_END = "clubsByCenterId/{id}";

    // Constructor
    public ClubEndpoint(RequestSpecification specification) {
        super(specification);
    }

    public ClubResponseDto getClubById(int id) {
        return getClubById(id, HttpStatus.OK)
                .extract().as(ClubResponseDto.class);
    }

    // Get club by a certain id
    public ValidatableResponse getClubById(int id, HttpStatus status) {
        LOGGER.info("Get Club by id [{}]", id);
        return get(
                this.specification,
                CLUB_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }

    public List<ClubResponseDto> getAllClubs() {
        return List.of(getAllClubs(HttpStatus.OK).extract().as(ClubResponseDto[].class));
    }

    public ValidatableResponse getAllClubs(HttpStatus status) {
        LOGGER.info("Get all Clubs");
        ValidatableResponse response = get(this.specification, CLUBS_END);
        response.statusCode(status.getCode());
        return response;
    }

    public List<ClubResponseDto> getClubsByCenterId(int id) {
        return List.of(getClubsByCenterId(id, HttpStatus.OK).extract().as(ClubResponseDto[].class));
    }

    public ValidatableResponse getClubsByCenterId(int id, HttpStatus status) {
        LOGGER.info("Get Clubs by center id [{}]", id);
        return get(
                this.specification,
                CLUBS_BY_CENTER_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }

}
