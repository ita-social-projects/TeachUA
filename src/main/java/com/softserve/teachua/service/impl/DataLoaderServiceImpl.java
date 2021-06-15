package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.databaseTransfer.ExcelConvertToFormatStringContactsData;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingData;
import com.softserve.teachua.dto.databaseTransfer.model.*;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.ExcelCenterEntity;
import com.softserve.teachua.model.ExcelClubEntity;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ExcelCenterEntityRepository;
import com.softserve.teachua.repository.ExcelClubEntityRepository;
import com.softserve.teachua.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * @author Vitalii Hapon
 */
@Service
@Slf4j
public class DataLoaderServiceImpl implements DataLoaderService {
    private static final long DEFAULT_USER_OWNER_ID = 1L;
    private static final String CENTER_DEFAULT_URL_WEB = "#";
    private static final String CENTER_DEFAULT_EMAIL = "email@mail.com";
    private static final String DEFAULT_CATEGORY_ICON_URL = "/static/images/categories/other.svg";
    private static final String DEFAULT_CATEGORY_BACKGROUND_COLOR = "#13C2C2";
    private static final String DEFAULT_CLUB_WORK_TIME = "09:00-16:00";
    private static final String DEFAULT_CLUB_URL_WEB = "#";
    private static final String DEFAULT_CLUB_URL_LOGO = "#";
    private static final String DEFAULT_CLUB_URL_BACKGROUND = "/static/images/club/bg_2.png";
    private static final String CLUB_DEFAULT_DESCRIPTION = "Опис відсутній";
    private static final String CENTER_DEFAULT_LOGO_URL = "https://www.logodesign.net/images/illustration-logo.png";
    private static final String DESCRIPTION_JSON_LEFT = "{\"blocks\":[{\"key\":\"etag9\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"8lltb\",\"text\":\" \",\"type\":\"atomic\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[{\"offset\":0,\"length\":1,\"key\":0}],\"data\":{}},{\"key\":\"98dtl\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9q9dc\",\"text\":\"";
    private static final String DESCRIPTION_JSON_RIGHT = "\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{\"0\":{\"type\":\"image\",\"mutability\":\"IMMUTABLE\",\"data\":{\"src\":\"https://linguapedia.info/wp-content/uploads/2015/05/history-of-ukrainian.jpg\",\"className\":\"edited-image edited-image-center\"}}}}";

    private final CategoryService categoryService;
    private final CenterService centerService;
    private final ClubService clubService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final UserService userService;
    private final CityService cityService;
    private final ClubRepository clubRepository;
    private final LocationService locationService;
    private final ExcelConvertToFormatStringContactsData contactsConverter;
    private final ExcelConvertToFormatStringContactsData excelContactsConverter;

    private final ExcelCenterEntityRepository excelCenterEntityRepository;
    private final ExcelClubEntityRepository excelClubEntityRepository;

    @Autowired
    public DataLoaderServiceImpl(CategoryService categoryService,
                                 CenterService centerService,
                                 ClubService clubService,
                                 DistrictService districtService,
                                 StationService stationService,
                                 UserService userService,
                                 CityService cityService,
                                 ClubRepository clubRepository,
                                 LocationService locationService,
                                 ExcelConvertToFormatStringContactsData contactsConverter,
                                 ExcelConvertToFormatStringContactsData excelContactsConverter,
                                 ExcelCenterEntityRepository excelCenterEntityRepository,
                                 ExcelClubEntityRepository excelClubEntityRepository) {
        this.categoryService = categoryService;
        this.centerService = centerService;
        this.clubService = clubService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.userService = userService;
        this.cityService = cityService;
        this.clubRepository = clubRepository;
        this.locationService = locationService;
        this.contactsConverter = contactsConverter;
        this.excelContactsConverter = excelContactsConverter;
        this.excelCenterEntityRepository = excelCenterEntityRepository;
        this.excelClubEntityRepository = excelClubEntityRepository;
    }

    public void loadToDatabase(ExcelParsingData excelParsingData) {
        Map<Long, Long> excelIdToDbId = new HashMap<>();
        Set<String> categoriesNames = new HashSet<>();

        // todo tmp load data is in parser
//        loadExcelEntityToDB(excelParsingData);

        log.info("=========LOADING DATA TO DB STEP: all locations form dto =========");
        log.info(excelParsingData.getLocations().toString());

        loadDistricts(excelParsingData);
        loadStations(excelParsingData);
        loadCategories(excelParsingData, categoriesNames);
        loadCenters(excelParsingData, excelIdToDbId);
        loadClubs(excelParsingData, excelIdToDbId, categoriesNames);
        loadLocations(excelParsingData);

    }

    private void loadExcelEntityToDB(ExcelParsingData excelParsingData){

        for (ExcelCenterEntity center : excelParsingData.getExcelCenters()) {
            excelCenterEntityRepository.save(center);
        }

        for (ExcelClubEntity club : excelParsingData.getExcelClubs()) {

            excelClubEntityRepository.save(club);
        }

    }

    private void loadLocations(ExcelParsingData excelParsingData) {
        log.info("==============load locations DataLoaderService =========");
        log.info("locations.length : "+excelParsingData.getLocations().size());

        for (LocationExcel location : excelParsingData.getLocations()) {
            log.info("(row 133, DataLoader : "+location.toString());
            try{

                String cityName = location.getCity();
                if( cityName == null || cityName.isEmpty()){
                    cityName = "Київ";
                }

                Long districtIdCheck = null;
                if(location.getDistrict() != null && districtService.getOptionalDistrictByName(location.getDistrict()).isPresent()) {
                    districtIdCheck = districtService.getOptionalDistrictByName(location.getDistrict()).get().getId();
                }

                Long stationIdCheck = null;
                if(location.getStation() != null & stationService.getOptionalStationByName(location.getStation()).isPresent()) {
                    stationIdCheck = stationService.getOptionalStationByName(location.getStation()).get().getId();
                }

                LocationProfile locationProfile = LocationProfile.builder()
                        .id(location.getId())
                        .address(location.getAddress())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .name(location.getName())
                        .cityId(cityService.getCityByName(cityName).getId())
                        .districtId(districtIdCheck)
                        .stationId(stationIdCheck)
                        .build();
                if (location.getClubExternalId() == null) {
                    locationProfile = locationProfile.withClubId(null);
                    if(location.getCenterExternalId() == null) {
                        log.info("location has no ref of club or center !!!");
                        throw new DataFormatException();
                    }else{
                        log.info("getCenterByExternalId = " + location.getCenterExternalId());
                        locationProfile = locationProfile.withCenterId(centerService.getCenterByExternalId(location.getCenterExternalId()).getId());
                    }
                } else {
                    log.info("Review Club Location location.getClubExternalId() = " + location.getClubExternalId());
                    while(clubService.getClubByClubExternalId(location.getClubExternalId()).size() == 0) {
                        location.setClubExternalId(location.getClubExternalId() - 1);
                    }
                    locationProfile = locationProfile.withClubId(clubService.getClubByClubExternalId(location.getClubExternalId()).get(0).getId());
                }
                log.info("LocationProfile before saving : "+locationProfile);

                locationService.addLocation(locationProfile);

                log.info("====== location added ==");
                log.info(location.getName()+" ");

            }catch (AlreadyExistException | NoSuchElementException | DataFormatException | NullPointerException e){
                log.info("AlreadyExist = " + location.getClubExternalId());
                log.info(e.getMessage());
            }
        }
    }

    private void loadCenters(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId) {

        log.info("======= load_CENTERS DataLoaderService =========");

        for (CenterExcel center : excelParsingData.getCenters()) {

            log.info("CENTER_EXCEL obj: "+ center.toString());
            try {

                SuccessCreatedCenter createdCenter = centerService.addCenter(CenterProfile
                        .builder()
                        .description(center.getDescription())
                        .user(userService.getUserById(DEFAULT_USER_OWNER_ID))
                        .name(center.getName())
                        .urlWeb(CENTER_DEFAULT_URL_WEB)
                        .urlLogo(CENTER_DEFAULT_LOGO_URL)
                        .contacts(excelContactsConverter.collectAllContactsData(center.getSite(),center.getPhone()))
                        .centerExternalId(center.getCenterExternalId())
                        .build());
                excelIdToDbId.put(center.getCenterExternalId(), createdCenter.getId());
            } catch (AlreadyExistException e) {
            	log.error("***###ERROR CENTER to DB: "+ center.toString());
                log.error("Trying to add already exists center from excel");
            }

        }
    }

    private void loadClubs(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId, Set<String> categories) {

        log.info("(row 219, DataLoader) ======= loadClubs DataLoaderService =========");
        for (ClubExcel club : excelParsingData.getClubs()) {
            log.info(club.toString());
            try {
                if (club.getAgeFrom() == null) {
                    club.setAgeFrom(2);
                    club.setAgeTo(18);
                }

                Center center = centerService.getCenterByExternalId(club.getCenterExternalId());
                Long realCenterId = null;

                if(center != null ){
                    realCenterId = center.getId();
                }

                //******************************* TODO

                ClubProfile clubProfile = ClubProfile
                        .builder()
                        .ageFrom(club.getAgeFrom())
                        .ageTo(club.getAgeTo())

                        .centerId(realCenterId)
                        .centerExternalId(club.getCenterExternalId())
                        .clubExternalId(club.getClubExternalId())
                        .description(DESCRIPTION_JSON_LEFT +
                                (club.getDescription().isEmpty() ?
                                        CLUB_DEFAULT_DESCRIPTION :
                                        club.getDescription()
                                                .replace("\"", "''")
                                                .replace("\\", "/")
                                                .replace("\n", " ")
                                                .replace("\r", " "))+ DESCRIPTION_JSON_RIGHT)


                        .name(club.getName())
                        .urlBackground(DEFAULT_CLUB_URL_BACKGROUND)
                        .urlLogo(DEFAULT_CLUB_URL_LOGO)
                        .categoriesName(getFullCategoryName(categories, club.getCategories()))
                        .contacts(contactsConverter.collectAllContactsData(club.getSite(),club.getPhone()))
                        .build();
                if(club.getCenterExternalId() == null) {
                    clubProfile = clubProfile.withCenterId(null);
                } else {
                    log.info(" was  PROBLEM POINT");
                    if(center == null) {
                        log.info("==(row-261,DataLoaderServiceImpl)==Center with external_id" + club.getCenterExternalId() + " is null");
                        clubProfile = clubProfile.withCenterId(null);
                    } else {
                        clubProfile = clubProfile.withCenterId(center.getId());
                    }
                }

                clubService.addClubsFromExcel(clubProfile);

            } catch (AlreadyExistException e) {
            	log.error("***###ERROR Club to DB: "+ club.toString());
                log.error(e.getMessage());
            }

        }
    }

    private void loadDistricts(ExcelParsingData excelParsingData) {
        for (DistrictExcel district : excelParsingData.getDistricts()) {
            try {
                districtService.addDistrict(DistrictProfile
                        .builder()
                        .cityName(district.getCity())
                        .name(district.getName())
                        .build());
            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists district from excel");
            }
        }
    }

    private void loadStations(ExcelParsingData excelParsingData) {
        for (StationExcel station : excelParsingData.getStations()) {
            try {
                stationService.addStation(StationProfile
                        .builder()
                        .cityName(cityService.getCityByName(station.getCity()).getName())
                        .name(station.getName())
                        .build());
            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists station from excel");
            }
        }
    }

    private List<String> getFullCategoryName(Set<String> categoriesNames, List<String> shortCategoryNames) {
        return categoriesNames.stream()
                .filter(s -> shortCategoryNames.stream()
                        .map(String::toLowerCase)
                        .anyMatch(b -> s.toLowerCase().contains(b)))
                .collect(Collectors.toList());
    }

    private void loadCategories(ExcelParsingData excelParsingData, Set<String> categoriesNames) {
        for (CategoryExcel category : excelParsingData.getCategories()) {
            try {
                categoriesNames.add(category.getName());
                categoryService.addCategory(CategoryProfile
                        .builder()
                        .backgroundColor(DEFAULT_CATEGORY_BACKGROUND_COLOR)
                        .description(category.getDescription())
                        .urlLogo(DEFAULT_CATEGORY_ICON_URL)
                        .name(category.getName())
                        .build());

            } catch (AlreadyExistException e) {
                log.warn("Trying to add already exists category from excel");
            }
        }
    }
}