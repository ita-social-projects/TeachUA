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

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubEndpoint.class);
    private static final String CLUBS_END = "clubs";
    private static final String CLUB_END = "club";
    private static final String CLUB_RESOURCE_END = "club/{id}";
    private static final String CLUBS_BY_CENTER_RESOURCE_END = "clubsByCenterId/{id}";

    // Constructor
    public ClubEndpoint(RequestSpecification specification) {
        super(specification);
    }

    public ClubResponseDto create(ClubResponseDto clubResponseDto) {
        return create(clubResponseDto, HttpStatus.CREATED)
                .extract().as(ClubResponseDto.class);
    }

    public ValidatableResponse create(ClubResponseDto clubResponseDto, HttpStatus status) {
        LOGGER.info("Create new Club");
        return post(
                this.specification,
                CLUB_END,
                clubResponseDto)
                .statusCode(status.getCode());
    }

    public ClubResponseDto updateAllFields(int id, ClubResponseDto clubResponseDto) {
        return updateAllFields(clubResponseDto, id, HttpStatus.OK)
                .extract().as(ClubResponseDto.class);
    }

    public ValidatableResponse updateAllFields(ClubResponseDto clubResponseDto, int id, HttpStatus status) {
        LOGGER.info("Update Club with id [{}]", id);
        return put(
                this.specification,
                CLUB_RESOURCE_END,
                clubResponseDto,
                id)
                .statusCode(status.getCode());
    }

    public ClubResponseDto updateSomeFields(int id, ClubResponseDto clubResponseDto) {
        return updateSomeFields(clubResponseDto, id, HttpStatus.OK)
                .extract().as(ClubResponseDto.class);
    }

    public ValidatableResponse updateSomeFields(ClubResponseDto clubResponseDto, int id, HttpStatus status) {
        LOGGER.info("Partially update Club with id [{}]", id);
        return patch(
                this.specification,
                CLUB_RESOURCE_END,
                clubResponseDto,
                id)
                .statusCode(status.getCode());
    }

    public ClubResponseDto getById(int id) {
        return getById(id, HttpStatus.OK)
                .extract().as(ClubResponseDto.class);
    }

    // Get club by a certain id
    public ValidatableResponse getById(int id, HttpStatus status) {
        LOGGER.info("Get Club by id [{}]", id);
        return get(
                this.specification,
                CLUB_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }

    public List<ClubResponseDto> getAll() {
        return List.of(getAll(HttpStatus.OK).extract().as(ClubResponseDto[].class));
    }

    public ValidatableResponse getAll(HttpStatus status) {
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

    public ClubResponseDto deleteById(int id) {
        return deleteById(id, HttpStatus.OK)
                .extract().as(ClubResponseDto.class);
    }

    // Get club by a certain id
    public ValidatableResponse deleteById(int id, HttpStatus status) {
        LOGGER.info("Delete Club by id [{}]", id);
        return delete(
                this.specification,
                CLUB_RESOURCE_END,
                id)
                .statusCode(status.getCode());
    }

}
