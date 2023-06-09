package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.database_transfer.ExcelConvertToFormatStringContactsData;
import com.softserve.teachua.dto.database_transfer.ExcelParsingData;
import com.softserve.teachua.dto.database_transfer.model.*;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.City;
import com.softserve.teachua.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.zip.DataFormatException;

@Service
@Slf4j
public class DataLoaderServiceImpl implements DataLoaderService {
    private static final long DEFAULT_USER_OWNER_ID = 1L;
    private static final String CENTER_DEFAULT_URL_WEB = "#";
    private static final String APOSTROPHES_CHARS = "[’']";
    private static final String APOSTROPH_CHAR_ENG = "'";
    private static final String APOSTROPH_CHAR_UA = "’";
    private static final String DEFAULT_CATEGORY_ICON_URL = "/static/images/categories/other.svg";
    private static final String DEFAULT_CATEGORY_BACKGROUND_COLOR = "#13C2C2";
    private static final String DEFAULT_CLUB_URL_LOGO = "#";
    private static final String DEFAULT_CLUB_URL_BACKGROUND = "/static/images/club/bg_2.png";
    private static final String CLUB_DEFAULT_DESCRIPTION = "Опис відсутній";
    private static final String CENTER_DEFAULT_LOGO_URL = "https://www.logodesign.net/images/illustration-logo.png";
    private static final String DESCRIPTION_JSON_LEFT = "{\"blocks\":[{\"key\":\"etag9\",\"text\":\"\",\"type\":"
            + "\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":"
            + "\"8lltb\",\"text\":\" \",\"type\":\"atomic\",\"depth\":0,\"inlineStyleRanges\":[],"
            + "\"entityRanges\":[{\"offset\":0,\"length\":1,\"key\":0}],\"data\":{}},"
            + "{\"key\":\"98dtl\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],"
            + "\"entityRanges\":[],\"data\":{}},{\"key\":\"9q9dc\",\"text\":\"";
    private static final String DESCRIPTION_JSON_RIGHT = "\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges"
            + "\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{\"0\":{\"type\":\"image\",\"mutability\":"
            + "\"IMMUTABLE\",\"data\":{\"src\":"
            + "\"https://linguapedia.info/wp-content/uploads/2015/05/history-of-ukrainian.jpg\","
            + "\"className\":\"edited-image edited-image-center\"}}}}";

    private final CategoryService categoryService;
    private final CenterService centerService;
    private final ClubService clubService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final CityService cityService;
    private final LocationService locationService;
    private final ExcelConvertToFormatStringContactsData contactsConverter;
    private final ExcelConvertToFormatStringContactsData excelContactsConverter;

    @Autowired
    public DataLoaderServiceImpl(CategoryService categoryService, CenterService centerService, ClubService clubService,
                                 DistrictService districtService, StationService stationService,
                                 CityService cityService, LocationService locationService,
                                 ExcelConvertToFormatStringContactsData contactsConverter,
                                 ExcelConvertToFormatStringContactsData excelContactsConverter) {
        this.categoryService = categoryService;
        this.centerService = centerService;
        this.clubService = clubService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.cityService = cityService;
        this.locationService = locationService;
        this.contactsConverter = contactsConverter;
        this.excelContactsConverter = excelContactsConverter;
    }

    @Override
    public void loadToDatabase(ExcelParsingData excelParsingData) {
        log.debug("=========LOADING DATA TO DB STEP: all locations form dto =========");
        log.debug("{}", excelParsingData.getLocations().toString());

        loadDistricts(excelParsingData);
        loadStations(excelParsingData);
        Set<String> categoriesNames = new HashSet<>();
        Map<Long, Long> excelIdToDbId = new HashMap<>();
        loadCategories(excelParsingData, categoriesNames);
        loadCenters(excelParsingData, excelIdToDbId);
        loadClubs(excelParsingData, categoriesNames);
        createLocations(excelParsingData.getLocations());
    }

    private void createLocations(List<LocationExcel> locations) {
        locations.forEach(location -> {
            try {
                LocationProfile locationProfile = buildLocationProfile(location);
                locationService.addLocation(locationProfile);
                log.debug("Location added {}", location.getName());
            } catch (DataFormatException | AlreadyExistException | NoSuchElementException
                     | NullPointerException | NotExistException e) {
                log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected exception ", e);
            }
        });
    }

    private LocationProfile buildLocationProfile(LocationExcel location) throws DataFormatException, NotExistException {
        LocationProfile locationProfile = getLocationProfile(location);

        locationProfile = location.getClubExternalId() == null
                ? setLocationProfileWithoutClubId(location, locationProfile)
                : setLocationProfileWithClubId(location, locationProfile);

        return locationProfile;
    }

    private LocationProfile setLocationProfileWithoutClubId(
            LocationExcel location,
            LocationProfile locationProfile
    ) throws DataFormatException, NotExistException {
        locationProfile = locationProfile.withClubId(null);

        Long locationCenterId = Optional.ofNullable(location.getCenterExternalId())
                .orElseThrow(() -> {
                    String message = "Location has no ref of club or center !!!";
                    log.warn("{}", message);
                    return new DataFormatException(message);
                });
        Long centerId = centerService.getCenterByExternalId(locationCenterId).getId();

        return locationProfile.withCenterId(centerId);
    }

    private LocationProfile setLocationProfileWithClubId(
            LocationExcel location,
            LocationProfile locationProfile
    ) throws NotExistException {
        while (clubService.getClubByClubExternalId(location.getClubExternalId()).isEmpty()) {
            location.setClubExternalId(location.getClubExternalId() - 1);
        }
        locationProfile = locationProfile.withClubId(
                clubService.getClubByClubExternalId(location.getClubExternalId())
                        .get(0)
                        .getId()
        );

        return locationProfile;
    }


    private LocationProfile getLocationProfile(LocationExcel location) throws NotExistException {
        String cityName = location.getCity();
        if (cityName == null || cityName.isEmpty()) {
            cityName = "Київ";
        }

        Long districtIdCheck = null;
        if (StringUtils.isNotEmpty(location.getDistrict())) {
            districtIdCheck = districtService.getOptionalDistrictByName(location
                            .getDistrict().replaceAll(APOSTROPHES_CHARS, APOSTROPH_CHAR_UA))
                    .orElse(districtService.getOptionalDistrictByName(location
                                    .getDistrict().replaceAll(APOSTROPHES_CHARS, APOSTROPH_CHAR_ENG))
                                    .orElseThrow(() -> new NotExistException("District has not found -" + location.getDistrict()))
                    ).getId();
        }

        Long stationIdCheck = null;
        if (StringUtils.isNotEmpty(location.getStation())) {
            stationIdCheck = stationService.getOptionalStationByName(location.getStation())
                    .orElseThrow(() -> new NotExistException("Station has not found -" + location.getStation()))
                    .getId();
        }
        return LocationProfile.builder().id(location.getId())
                .address(location.getAddress()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).name(location.getName())
                .cityId(cityService.getCityByName(cityName).getId())
                .districtId(districtIdCheck)
                .stationId(stationIdCheck).build();
    }

    private void loadCenters(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId) {
        log.debug("======= load_CENTERS DataLoaderService =========");

        for (CenterExcel center : excelParsingData.getCenters()) {
            log.debug("CENTER_EXCEL obj: " + center.toString());
            try {
                SuccessCreatedCenter createdCenter = centerService.addCenter(CenterProfile.builder()
                        .description(center.getDescription()).userId(DEFAULT_USER_OWNER_ID).name(center.getName())
                        .urlWeb(CENTER_DEFAULT_URL_WEB).urlLogo(CENTER_DEFAULT_LOGO_URL)
                        .contacts(excelContactsConverter.collectAllContactsData(center.getSite(), center.getPhone()))
                        .centerExternalId(center.getCenterExternalId()).build());
                excelIdToDbId.put(center.getCenterExternalId(), createdCenter.getId());
            } catch (AlreadyExistException e) {
                log.error("***###ERROR CENTER to DB: " + center);
                log.error("Trying to add already exists center from excel");
            } catch (Exception constraintViolationException) {
                log.error("Validation in description center: " + center.getCenterExternalId());
                log.error(constraintViolationException.getMessage());
            }
        }
    }

    private void loadClubs(ExcelParsingData excelParsingData, Set<String> categories) {
        log.debug("(row 219, DataLoader) ======= loadClubs DataLoaderService =========");
        for (ClubExcel club : excelParsingData.getClubs()) {
            log.debug(club.toString());
            if (club.getAgeFrom() == null) {
                club.setAgeFrom(2);
                club.setAgeTo(18);
            }
            try {
                Center center = centerService.getCenterByExternalId(club.getCenterExternalId());
                ClubProfile clubProfile = getClubProfile(categories, club, center);
                if (club.getCenterExternalId() == null) {
                    clubProfile = clubProfile.withCenterId(null);
                } else {
                    log.debug(" was  PROBLEM POINT");
                    if (center == null) {
                        log.debug("==(row-261,DataLoaderServiceImpl)==Center with external_id"
                                + club.getCenterExternalId() + " is null");
                        clubProfile = clubProfile.withCenterId(null);
                    } else {
                        clubProfile = clubProfile.withCenterId(center.getId());
                    }
                }

                clubService.addClubsFromExcel(clubProfile);
            } catch (AlreadyExistException e) {
                log.error("***###ERROR Club to DB: " + club);
                log.error(e.getMessage());
            } catch (Exception constraintViolationException) {
                log.error("Validation in description: " + club.getClubExternalId());
                log.error(constraintViolationException.getMessage());
            }
        }
    }

    private ClubProfile getClubProfile(Set<String> categories, ClubExcel club, Center center) {
        Long realCenterId = null;
        if (center != null) {
            realCenterId = center.getId();
        }
        return ClubProfile.builder().ageFrom(club.getAgeFrom()).ageTo(club.getAgeTo())
                .centerId(realCenterId).centerExternalId(club.getCenterExternalId())
                .clubExternalId(club.getClubExternalId())
                .description(
                        DESCRIPTION_JSON_LEFT
                                + (club.getDescription().isEmpty() ? CLUB_DEFAULT_DESCRIPTION
                                : club.getDescription().replace("\"", "''").replace("\\", "/")
                                .replace("\n", " ").replace("\r", " "))
                                + DESCRIPTION_JSON_RIGHT)
                .name(club.getName()).urlBackground(DEFAULT_CLUB_URL_BACKGROUND).urlLogo(DEFAULT_CLUB_URL_LOGO)
                .categoriesName(getFullCategoryName(categories, club.getCategories()))
                .contacts(contactsConverter.collectAllContactsData(club.getSite(), club.getPhone())).build();
    }

    private void loadDistricts(ExcelParsingData excelParsingData) {
        for (DistrictExcel district : excelParsingData.getDistricts()) {
            try {
                districtService.addDistrict(
                        DistrictProfile.builder().cityName(district.getCity()).name(district.getName()).build());
            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists district from excel");
            }
        }
    }

    private void loadStations(ExcelParsingData excelParsingData) {
        for (StationExcel station : excelParsingData.getStations()) {
            try {
                City city = cityService.getCityByName(station.getCity());
                stationService.addStation(StationProfile.builder().cityName(city.getName())
                        .districtName(districtService.getListOfDistrictsByCityName(city.getName()).get(0).getName())
                        .name(station.getName()).build());
            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists station from excel");
            }
        }
    }

    private List<String> getFullCategoryName(Set<String> categoriesNames, List<String> shortCategoryNames) {
        return categoriesNames.stream().filter(
                        s -> shortCategoryNames.stream()
                                .map(String::toLowerCase).anyMatch(b -> s.toLowerCase().contains(b)))
                .toList();
    }

    private void loadCategories(ExcelParsingData excelParsingData, Set<String> categoriesNames) {
        for (CategoryExcel category : excelParsingData.getCategories()) {
            try {
                categoriesNames.add(category.getName());
                categoryService.addCategory(CategoryProfile.builder().backgroundColor(DEFAULT_CATEGORY_BACKGROUND_COLOR)
                        .description(category.getDescription()).urlLogo(DEFAULT_CATEGORY_ICON_URL)
                        .name(category.getName()).build());
            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists category from excel");
            }
        }
    }
}
