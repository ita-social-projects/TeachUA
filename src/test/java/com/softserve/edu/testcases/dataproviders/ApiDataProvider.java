package com.softserve.edu.testcases.dataproviders;

import org.testng.annotations.DataProvider;

public class ApiDataProvider {

    private static final int CENTER_ID = 11;

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "allClubs")
    public static Object[][] getAllClubs() {
        // Query to sort clubs by rates in the DB
        return new Object[][] {
                // Query to get all the clubs with their id from the DB
                { "SELECT name\n" +
                        "FROM clubs;" }
        };
    }

    @DataProvider(name = "clubsByCenterId")
    public static Object[][] getClubsByCenterId() {
        // Query to sort clubs by rates in the DB
        return new Object[][] {
                // Query to get all the clubs with their id from the DB
                { "SELECT name\n" +
                        "FROM clubs\n" +
                        "WHERE center_id = " + CENTER_ID +";" }
        };
    }

}
