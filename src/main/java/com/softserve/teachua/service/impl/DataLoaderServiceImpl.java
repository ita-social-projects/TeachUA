package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.category.CategoryProfile;
import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
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
import com.softserve.teachua.repository.ClubRepository;
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
    public static final String CENTER_DEFAULT_URL_WEB = "#";
    public static final String CENTER_DEFAULT_EMAIL = "email@mail.com";
    public static final String DEFAULT_CATEGORY_ICON_URL = "/static/images/categories/other.svg";
    public static final String DEFAULT_CATEGORY_BACKGROUND_COLOR = "#13C2C2";
    public static final String DEFAULT_CLUB_WORK_TIME = "09:00-16:00";
    public static final String DEFAULT_CLUB_URL_WEB = "#";
    public static final String DEFAULT_CLUB_URL_LOGO = "#";
    public static final String DEFAULT_CLUB_URL_BACKGROUND = "/static/images/club/bg_2.png";
    private static String CATEGORY_DEFAULT_LOGO_URL = "/static/images/categories/sport.svg";
    private static String CENTER_DEFAULT_LOGO_URL = "https://www.logodesign.net/images/illustration-logo.png";
    private static String DESCRIPTION_JSON_LEFT = "{\"blocks\":[{\"key\":\"etag9\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"8lltb\",\"text\":\" \",\"type\":\"atomic\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[{\"offset\":0,\"length\":1,\"key\":0}],\"data\":{}},{\"key\":\"98dtl\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9q9dc\",\"text\":\"";
    private static String DESCRIPTION_JSON_RIGHT = "\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{\"0\":{\"type\":\"image\",\"mutability\":\"IMMUTABLE\",\"data\":{\"src\":\"https://linguapedia.info/wp-content/uploads/2015/05/history-of-ukrainian.jpg\",\"className\":\"edited-image edited-image-center\"}}}}";

    private final CategoryService categoryService;
    private final CenterService centerService;
    private final ClubService clubService;
    private final DistrictService districtService;
    private final StationService stationService;
    private final UserService userService;
    private final CityService cityService;
    private final ClubRepository clubRepository;

    @Autowired
    public DataLoaderServiceImpl(CategoryService categoryService,
                                 CenterService centerService,
                                 ClubService clubService,
                                 DistrictService districtService,
                                 StationService stationService,
                                 UserService userService,
                                 CityService cityService, ClubRepository clubRepository) {
        this.categoryService = categoryService;
        this.centerService = centerService;
        this.clubService = clubService;
        this.districtService = districtService;
        this.stationService = stationService;
        this.userService = userService;
        this.cityService = cityService;
        this.clubRepository = clubRepository;
    }


    public void loadToDatabase(ExcelParsingData excelParsingData) {
        Map<Long, Long> excelIdToDbId = new HashMap<>();
        Set<String> categoriesNames = new HashSet<>();
        loadDistricts(excelParsingData);
        loadStations(excelParsingData);
        loadCategories(excelParsingData, categoriesNames);
        loadCenters(excelParsingData, excelIdToDbId);
        loadClubs(excelParsingData, excelIdToDbId, categoriesNames);
    }

    private void loadCenters(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId) {
        for (CenterExcel center : excelParsingData.getCenters()) {
            try {
                if (center.getLongitude() == null || center.getAltitude() == null)
                    continue;

                try {
                    centerService.deleteCenterById(centerService.getCenterByName(center.getName()).getId());
                } catch (NotExistException| DatabaseRepositoryException e) {
                    // Do nothing if there is no such center
                    // or if center has any relationships
                }

                SuccessCreatedCenter createdCenter = centerService.addCenter(CenterProfile
                        .builder()
                        .id(center.getId())
                        .address(center.getAddress())
                        .description(center.getDescription())
                        .longitude(center.getLongitude())
                        .latitude(center.getAltitude())
                        .phones(center.getPhone())
                        .socialLinks(center.getSite())
                        .user(userService.getUserById(DEFAULT_USER_OWNER_ID))
                        .name(center.getName())
                        .urlWeb(CENTER_DEFAULT_URL_WEB)
                        .email(CENTER_DEFAULT_EMAIL)
                        .urlLogo(CENTER_DEFAULT_LOGO_URL)
                        .build());
                excelIdToDbId.put(center.getId(), createdCenter.getId());
            } catch (AlreadyExistException e) {
                log.error("Trying to add already exists center from excel");
            }

        }
    }

    private void loadClubs(ExcelParsingData excelParsingData, Map<Long, Long> excelIdToDbId, Set<String> catgories) {
        for (ClubExcel club : excelParsingData.getClubs()) {
            try {
                if (club.getAgeFrom() == null) {
                    club.setAgeFrom(2);
                    club.setAgeTo(16);
                }
                if (club.getCity().isEmpty()) {
                    club.setCity("Київ");
                }
                try {
                    clubService.deleteClubById(clubService.getClubByName(club.getName()).getId());
                } catch (NotExistException|DatabaseRepositoryException e) {
                    // Do nothing if there is no such club
                    // or if club has any relationships
                }
                LocationProfile locationProfile = LocationProfile
                        .builder()
                        .address(club.getAddress())
                        .latitude(club.getAltitude())
                        .longitude(club.getLongitude())
                        .cityName(club.getCity())
                        .phone(club.getPhone())
                        .districtName(club.getDistrict())
                        .stationName(club.getStation())
                        .build();

                List<LocationProfile> locations = new ArrayList<>();
                locations.add(locationProfile);
                SuccessCreatedClub createdClub = clubService.addClub(ClubProfile
                        .builder()
                        .ageFrom(club.getAgeFrom())
                        .ageTo(club.getAgeTo())
                        .locations(locations)


                        .description(DESCRIPTION_JSON_LEFT +
                                (club.getDescription().isEmpty() ? "Опис" : club.getDescription().isEmpty()) +
                                DESCRIPTION_JSON_RIGHT)

                        .name(club.getName())
                        .urlBackground(DEFAULT_CLUB_URL_BACKGROUND)
                        .urlLogo(DEFAULT_CLUB_URL_LOGO)
                        .categoriesName(getFullCategoryName(catgories, club.getCategories()))
                        .build());

                clubRepository.flush();

                Club addedClub = clubService.getClubById(createdClub.getId());
                addedClub.setUrlWeb(DEFAULT_CLUB_URL_WEB);
                addedClub.setWorkTime(DEFAULT_CLUB_WORK_TIME);
                addedClub.setIsApproved(true);

                addedClub.setUser(userService.getUserById(DEFAULT_USER_OWNER_ID));

                if (excelIdToDbId.containsKey(club.getId())) {
                    Center center = centerService.getCenterById(excelIdToDbId.get(club.getId()));
                    addedClub.setCenter(center);
                }
                clubRepository.save(addedClub);
            } catch (AlreadyExistException e) {
                log.error("Trying to add already exists club from excel");
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
                        .city(cityService.getCityByName(station.getCity()))
                        .name(station.getName())
                        .build());
            } catch (AlreadyExistException e) {
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
