package com.softserve.edu.testcases.dataproviders;

import com.softserve.edu.testcases.enums.KyivDistricts;
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

}
