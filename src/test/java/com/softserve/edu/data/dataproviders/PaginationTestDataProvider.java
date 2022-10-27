package com.softserve.edu.data.dataproviders;

import com.softserve.edu.data.Locations;
import org.testng.annotations.DataProvider;

public class PaginationTestDataProvider {

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "pagination")
    public static Object[][] getPaginationData() {
        // Query to find total number of clubs present in DB for defined location
        return new Object[][] {
                {"SELECT COUNT(DISTINCT c.name)\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name='" + Locations.KYIV + "';"}
        };
    }

}
