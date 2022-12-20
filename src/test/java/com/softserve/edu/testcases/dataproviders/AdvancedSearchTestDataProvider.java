package com.softserve.edu.testcases.dataproviders;

import com.softserve.edu.testcases.enums.Categories;
import com.softserve.edu.testcases.enums.KyivDistricts;
import com.softserve.edu.testcases.enums.KyivMetroStations;
import com.softserve.edu.testcases.enums.Locations;
import org.testng.annotations.DataProvider;

public class AdvancedSearchTestDataProvider {

    private static final String KYIV_CITY = Locations.DEFAULT_LOCATION.toString();
    private static final String BORYSPILSKA_METRO_STATION = KyivMetroStations.BORYSPILSKA.toString();
    private static final String BERESTEYSKA_METRO_STATION = KyivMetroStations.BERESTEYSKA.toString();
    private static final String VYRLITSA_METRO_STATION = KyivMetroStations.VYRLITSA.toString();
    private static final String SVYATOSHINSKY_DISTRICT = KyivDistricts.SVYATOSHINSKY.toString();
    private static final String DESNYANSKYI_DISTRICT = KyivDistricts.DESNYANSKYI.toString();

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "sortClubByRate")
    public static Object[][] getClubSortedByRate() {
        // Query to sort clubs by rates in the DB
        return new Object[][] {
                { "SELECT c.name\n" +
                        "FROM (\n" +
                        "\tSELECT name, rating, id\n" +
                        "\t FROM clubs\n" +
                        "\t GROUP BY rating, id, name\n" +
                        ") AS c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "GROUP BY c.name, c.rating, c.id\n" +
                        "ORDER BY c.rating ASC, c.id;",
                  // Query to sort clubs by rates and in descending order in the DB
                "SELECT c.name\n" +
                        "FROM (\n" +
                        "\tSELECT name, rating, id\n" +
                        "\t FROM clubs\n" +
                        "\t GROUP BY rating, id, name\n" +
                        ") AS c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "GROUP BY c.name, c.rating, c.id\n" +
                        "ORDER BY c.rating DESC, c.id;" }
        };
    }

    @DataProvider(name = "sortClubAlphabetically")
    public static Object[][] getClubSortedAlphabetically() {
        return new Object[][] {
                // Query to sort clubs alphabetically in ascending order in the DB
                { "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "ORDER BY c.name;",
                        // Query to sort clubs alphabetically in descending order in the DB
                        "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "ORDER BY c.name DESC;"}
        };
    }

    @DataProvider(name = "places")
    public static Object[][] getPlaces() {
        return new Object[][] {
                // Queries to sort clubs by city in the DB
                { "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "ORDER BY c.name ASC;",
                // Queries to sort clubs by city and district in the DB
                "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND ds.name = '" + SVYATOSHINSKY_DISTRICT + "'\n" +
                        "ORDER BY c.name ASC;",
                        // Queries to sort clubs by city and district in the DB
                "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND ds.name = '" + DESNYANSKYI_DISTRICT + "'\n" +
                        "ORDER BY c.name ASC;" }
        };
    }

    @DataProvider(name = "childAge")
    public static Object[][] getChildAge() {
        return new Object[][] {
                // Queries to sort clubs by city in the DB
                { "0" },
                { "1" },
                { "19" },
                { "115" },
                { "11років" },
                { "8 #" },
                { "2,5" },
                { "#$%^&" },
                { "qw" }
        };
    }

    @DataProvider(name = "stations")
    public static Object[][] getMetroStations() {
        return new Object[][] {
                {KYIV_CITY,
                        BERESTEYSKA_METRO_STATION,
                        // Query to find centers by city and metro station in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT id, name\n" +
                                "\tFROM centers\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND st.name = '" + BERESTEYSKA_METRO_STATION + "'\n" +
                                "GROUP BY c.id, c.name;",
                        VYRLITSA_METRO_STATION,
                        // Query to find centers by city and another metro station in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT id, name\n" +
                                "\tFROM centers\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND st.name = '" + VYRLITSA_METRO_STATION + "'\n" +
                                "GROUP BY c.id, c.name;" }
        };
    }

    @DataProvider(name = "district")
    public static Object[][] getDistricts() {
        return new Object[][] {
                {KYIV_CITY,
                        DESNYANSKYI_DISTRICT,
                        // Query to find centers by city and district in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT id, name\n" +
                                "\tFROM centers\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND ds.name = '" + DESNYANSKYI_DISTRICT + "'\n" +
                                "GROUP BY c.name, c.id;" }
        };
    }

    @DataProvider(name = "clubStations")
    public static Object[][] getClubMetroStations() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find centers by city and metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND st.name = '" + BORYSPILSKA_METRO_STATION + "';",
                        // Query to find centers by city and another metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND st.name = '" + VYRLITSA_METRO_STATION + "';" }
        };
    }

    @DataProvider(name = "clubDistricts")
    public static Object[][] getClubDistricts() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find clubs by city and district in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND ds.name = '" + DESNYANSKYI_DISTRICT + "';" }
        };
    }

    @DataProvider(name = "sortCentersByRating")
    public static Object[][] getCentersSortedByRating() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find sorted centers by rating in ascending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.name, rating\n" +
                                "ORDER BY rating;",
                        // Query to find sorted centers by rating in descending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.name, rating\n" +
                                "ORDER BY rating DESC;" }
        };
    }

    @DataProvider(name = "alphabeticCenterSort")
    public static Object[][] getCentersAlphabeticSort() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to sort centers by alphabet in ascending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.name\n" +
                                "ORDER BY c.name;",
                        // Query to sort centers by alphabet in descending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.name\n" +
                                "ORDER BY c.name DESC;" }
        };
    }

    @DataProvider(name = "clubsByLocation")
    public static Object[][] getClubsByLocation() {
        final String KHARKIV_CITY = Locations.KHARKIV.toString();
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find search that belongs to a certain region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "ORDER BY c.name;",
                        // Query to find search that belongs to another region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KHARKIV_CITY + "'\n" +
                                "ORDER BY c.name;" }
        };
    }

    @DataProvider(name = "centersByLocation")
    public static Object[][] getCentersByLocation() {
        final String KHARKIV_CITY = Locations.KHARKIV.toString();
        return new Object[][] {
                { KYIV_CITY,
                        // Query to find search that belongs to a certain region in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT id, name\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY id\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.id, c.name;",
                        // Query to find search that belongs to another region in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT id, name\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY id\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KHARKIV_CITY + "'\n" +
                                "GROUP BY c.id, c.name;" }
        };
    }

    @DataProvider(name = "clubsByCategories")
    public static Object[][] getClubsByCategories() {
        final Categories CATEGORY = Categories.PROGRAMMING;
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find search that belongs to a certain region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN club_category as cc ON c.id=cc.club_id\n" +
                                "INNER JOIN categories as cs ON cc.category_id=cs.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND cs.name = '" + CATEGORY + "';",
                        CATEGORY }
        };
    }

    @DataProvider(name = "remoteClubs")
    public static Object[][] getRemoteClubs() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find search that belongs to a certain region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.is_online = true;" }
        };
    }

    @DataProvider(name = "centersInCity")
    public static Object[][] getCentersInCity() {
        return new Object[][] {
                {KYIV_CITY,
                        // Query to find centers in specific city in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, id\n" +
                                "\tFROM centers\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "GROUP BY c.name, c.id\n" +
                                "ORDER BY c.id;" }
        };
    }

    @DataProvider(name = "validChildAge")
    public static Object[][] getValidChildAge() {
        final String MINIMUM_AGE = "2";
        final String ALMOST_MINIMUM_AGE = "3";
        final String ALMOST_MAXIMUM_AGE = "17";
        final String MAXIMUM_AGE = "18";
        return new Object[][] {
                // Queries to sort clubs by child age in the DB
                { KYIV_CITY,
                        MINIMUM_AGE,
                        // Query to find centers in specific city in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.age_from <= " + MINIMUM_AGE + " AND c.age_to >= " + MINIMUM_AGE + ";" },
                { KYIV_CITY,
                        ALMOST_MINIMUM_AGE,
                        // Query to find centers in specific city in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.age_from <= " + ALMOST_MINIMUM_AGE + " AND c.age_to >= " + ALMOST_MINIMUM_AGE + ";" },
                { KYIV_CITY,
                        ALMOST_MAXIMUM_AGE,
                        // Query to find centers in specific city in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.age_from <= " + ALMOST_MAXIMUM_AGE + " AND c.age_to >= " + ALMOST_MAXIMUM_AGE + ";" },
                { KYIV_CITY,
                        MAXIMUM_AGE,
                        // Query to find centers in specific city in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.age_from <= " + MAXIMUM_AGE + " AND c.age_to >= " + MAXIMUM_AGE + ";" }
        };
    }

}
