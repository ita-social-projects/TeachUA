package com.softserve.edu.data.dataproviders;

import com.softserve.edu.data.Locations;
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

}
