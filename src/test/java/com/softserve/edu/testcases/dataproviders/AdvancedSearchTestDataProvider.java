package com.softserve.edu.testcases.dataproviders;

import com.softserve.edu.testcases.enums.KyivDistricts;
import com.softserve.edu.testcases.enums.KyivMetroStations;
import com.softserve.edu.testcases.enums.Locations;
import org.testng.annotations.DataProvider;

public class AdvancedSearchTestDataProvider {

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "rateAscendingSort")
    public static Object[][] getRateAscendingSort() {
        // Query to sort clubs by rates in the DB
        return new Object[][] {
                { "SELECT name\n" +
                        "FROM (\n" +
                        "\tSELECT DISTINCT name, rating, id\n" +
                        "\t FROM clubs\n" +
                        "\t GROUP BY rating, id, name\n" +
                        ") AS c\n" +
                        "WHERE name IS NOT NULL\n" +
                        "ORDER BY rating ASC, id;",
                  // Query to sort clubs by rates and in ascending order in the DB
                "SELECT name\n" +
                        "FROM (\n" +
                        "\tSELECT DISTINCT name, rating, id\n" +
                        "\t FROM clubs\n" +
                        "\t GROUP BY rating, id, name\n" +
                        ") AS c\n" +
                        "ORDER BY rating DESC, id;" }
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
                        "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                        "ORDER BY c.name ASC;",
                // Queries to sort clubs by city and district in the DB
                "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                        "WHERE ct.name = 'Київ'\n" +
                        "AND ds.name = '" + KyivDistricts.SVYATOSHINSKY + "'\n" +
                        "ORDER BY c.name ASC;",
                        // Queries to sort clubs by city and district in the DB
                "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                        "WHERE ct.name = 'Київ'\n" +
                        "AND ds.name = '" + KyivDistricts.DESNYANSKYI + "'\n" +
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
                { Locations.KYIV.toString(),
                        // Query to find centers by city and metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM centers as c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND st.name = '" + KyivMetroStations.ARSENALNA + "';",
                        // Query to find centers by city and another metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM centers as c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND st.name = '" + KyivMetroStations.VYRLITSA + "';" }
        };
    }

    @DataProvider(name = "district")
    public static Object[][] getDistricts() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                  KyivDistricts.DESNYANSKYI.toString(),
                        // Query to find centers by city and district in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM centers as c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND ds.name = '" + KyivDistricts.DESNYANSKYI + "';" }
        };
    }

    @DataProvider(name = "clubStations")
    public static Object[][] getClubMetroStations() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                        // Query to find centers by city and metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND st.name = '" + KyivMetroStations.ARSENALNA + "';",
                        // Query to find centers by city and another metro station in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN stations as st ON l.station_id=st.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND st.name = '" + KyivMetroStations.VYRLITSA + "';" }
        };
    }

    @DataProvider(name = "clubDistricts")
    public static Object[][] getClubDistricts() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                        // Query to find clubs by city and district in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "INNER JOIN districts as ds ON l.district_id=ds.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "AND ds.name = '" + KyivDistricts.DESNYANSKYI + "';" }
        };
    }

    @DataProvider(name = "sortCentersByRating")
    public static Object[][] getCentersSortedByRating() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                        // Query to find sorted centers by rating in ascending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
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
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "GROUP BY c.name, rating\n" +
                                "ORDER BY rating DESC;" }
        };
    }

    @DataProvider(name = "alphabeticCenterSort")
    public static Object[][] getCentersAlphabeticSort() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                        // Query to sort centers by alphabet in ascending order in the DB
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, rating, id\n" +
                                "\tFROM centers\n" +
                                "\tORDER BY rating\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.center_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
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
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "GROUP BY c.name\n" +
                                "ORDER BY c.name DESC;" }
        };
    }

    @DataProvider(name = "clubsByLocation")
    public static Object[][] getClubsByLocation() {
        return new Object[][] {
                { Locations.KYIV.toString(),
                        // Query to find search that belongs to a certain region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + Locations.KYIV + "'\n" +
                                "ORDER BY c.name;",
                        // Query to find search that belongs to another region in the DB
                        "SELECT DISTINCT c.name\n" +
                                "FROM clubs as c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + Locations.KHARKIV + "'\n" +
                                "ORDER BY c.name;" }
        };
    }

}
