package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
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
import com.softserve.teachua.model.Club;
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

        for (LocationExcel location : excelParsingData.getLocations()) {
            log.info("==============load locations DataLoaderService =========");
            log.info(location.toString());
            try{
                LocationProfile locationProfile = LocationProfile.builder()
                        .id(location.getId())
                        .address(location.getAddress())
                        .longitude(location.getLongitude())
                        .latitude(location.getLatitude())
                        .name(location.getName())
                        .cityId(cityService.getCityByName(location.getCity()).getId())
                        .districtId(districtService.getOptionalDistrictByName(location.getDistrict()).get().getId())
                        .stationId(stationService.getOptionalStationByName(location.getStation()).get().getId())
//                        .centerId(location.getCenterId())
//                        .clubId(location.getClubId())
                        .build();
                if (location.getClubExternalId() == null) {
                    locationProfile.withClubId(null);
                    if(location.getCenterExternalId() == null) {
                        locationProfile.withCenterId(null);
                    }
                } else {
                    locationProfile.withClubId(clubService.getClubByClubExternalId(location.getClubExternalId()).getId());
                }
                locationService.addLocation(locationProfile);
                log.info("====== location added ==");
                log.info(location.getName()+" ");

            }catch (AlreadyExistException | NoSuchElementException e){
                log.info(e.getMessage());
            }
        }
    }

    private void loadCenters(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId) {

        for (CenterExcel center : excelParsingData.getCenters()) {
            try {
//                if (center.getLongitude() == null || center.getLatitude() == null)
//                    continue;

                try {
                    centerService.deleteCenterById(center.getId());

                } catch (NotExistException | DatabaseRepositoryException e) {
                    // Do nothing if there is no such center
                    // or if center has any relationships
                }

                SuccessCreatedCenter createdCenter = centerService.addCenter(CenterProfile
                        .builder()
                        .id(center.getId())
                        .description(center.getDescription())
                        .user(userService.getUserById(DEFAULT_USER_OWNER_ID))
                        .name(center.getName())
                        .urlWeb(CENTER_DEFAULT_URL_WEB)
                        .urlLogo(CENTER_DEFAULT_LOGO_URL)
                        .contacts(excelContactsConverter.collectAllContactsData(center.getSite(),center.getPhone()))
                        .build());
                excelIdToDbId.put(center.getId(), createdCenter.getId());
            } catch (AlreadyExistException e) {
                log.error("Trying to add already exists center from excel");
            }

        }
    }

    private void loadClubs(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId, Set<String> categories) {

        log.info("============== loadClubs DataLoaderService =========");
        for (ClubExcel club : excelParsingData.getClubs()) {
            log.info(club.toString());
            try {
                if (club.getAgeFrom() == null) {
                    club.setAgeFrom(0);
                    club.setAgeTo(16);
                }
//                if (club.getCity().isEmpty()) {
//                    club.setCity("Київ");
//                }
//                try {
//                    clubService.deleteClubById(clubService.getClubByName(club.getName()).getId());
//                } catch (NotExistException | DatabaseRepositoryException e) {
//                    // Do nothing if there is no such club
//                    // or if club has any relationships
//                }
//                LocationProfile locationProfile = LocationProfile
//                        .builder()
//                        .clubId(club.getId())
//                        .address(club.getAddress())
//                        .latitude(club.getLatitude())
//                        .longitude(club.getLongitude())
//                        .cityId(cityService.getCityByName(club.getCity()).getId())
//                        .districtId(districtService.getDistrictByName(club.getDistrict()).getId())
//                        .stationId(stationService.getStationByName(club.getStation()).getId())
//                        .build();

//                List<LocationProfile> locations = new ArrayList<>();
//                locations.add(locationProfile);

                ClubProfile clubProfile = ClubProfile
                        .builder()
                        .ageFrom(club.getAgeFrom())
                        .ageTo(club.getAgeTo())

//                        .centerId(centerService.getCenterByExternalId(club.getCenterId()).getId())
                        .centerExternalId(club.getCenterExternalId())
                        .clubExternalId(club.getClubExternalId())
                        .description(DESCRIPTION_JSON_LEFT +
                                (club.getDescription().isEmpty() ?
                                        CLUB_DEFAULT_DESCRIPTION :
                                        club.getDescription()
                                                .replace("\"", "''")
                                                .replace("\\", "/")
                                                .replace('\n', ' '))+ DESCRIPTION_JSON_RIGHT)

                        .name(club.getName())
                        .urlBackground(DEFAULT_CLUB_URL_BACKGROUND)
                        .urlLogo(DEFAULT_CLUB_URL_LOGO)
                        .categoriesName(getFullCategoryName(categories, club.getCategories()))
                        .contacts(contactsConverter.collectAllContactsData(club.getSite(),club.getPhone()))
                        .build();
                if(club.getCenterExternalId() == null) {
                    clubProfile.withCenterId(null);
                } else {
                    Center center = centerService.getCenterByExternalId(club.getCenterExternalId());
                    if(center == null) {
                        log.info("Center with id" + club.getCenterExternalId() + " is null");
                        clubProfile.withCenterId(null);
                    } else {
                        clubProfile.withCenterId(center.getId());
                    }
                }
                Club createdClub = clubService.addClubsFromExcel(clubProfile);
//                clubRepository.flush();

//                Club addedClub = clubService.getClubById(createdClub.getId());
//                addedClub.setUrlWeb(DEFAULT_CLUB_URL_WEB);
//                addedClub.setWorkTime(DEFAULT_CLUB_WORK_TIME);
//                addedClub.setIsApproved(true);
//
//                addedClub.setUser(userService.getUserById(DEFAULT_USER_OWNER_ID));
//
//                if (excelIdToDbId.containsKey(club.getId())) {
//                    Center center = centerService.getCenterById(excelIdToDbId.get(club.getId()));
//                    addedClub.setCenter(center);
//                }
//                clubRepository.save(addedClub);
            } catch (AlreadyExistException e) {
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