package com.softserve.edu.testcases.dataproviders;

import com.softserve.edu.testcases.enums.Locations;
import com.softserve.edu.testcases.repositories.club.ClubRepository;
import org.testng.annotations.DataProvider;

public class BasicSearchTestDataProvider {

    private static final String KYIV_CITY = Locations.KYIV.toString();

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "specialCharactersTitle")
    public static Object[][] getTitlesWithSpecialCharacters() {
        final String PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS = "=,/ , , *, (, ), _, :, ;, #";
        // Query to find clubs that contain special characters in their titles in the DB
        return new Object[][] {
                { PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS,
                        "SELECT c.name\n" +
                                "FROM (\n" +
                                "\tSELECT name, id\n" +
                                "\t FROM clubs\n" +
                                "\t GROUP BY id, name\n" +
                                ") AS c\n" +
                                "INNER JOIN locations as l ON c.id=l.club_id\n" +
                                "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                                "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                                "AND c.name LIKE '%" + PARTIAL_CLUB_TITLE_WITH_SPECIAL_CHARACTERS + "%'\n" +
                                "ORDER BY c.id;" }
        };
    }

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "numberTitle")
    public static Object[][] getTitlesWithNumbers() {
        final String PARTIAL_CLUB_TITLE_WITH_NUMBERS = "2412";
        // Query to find clubs that contain numbers in their titles in the DB
        return new Object[][] {
                { PARTIAL_CLUB_TITLE_WITH_NUMBERS,
                        "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND c.name LIKE '%" + PARTIAL_CLUB_TITLE_WITH_NUMBERS + "%'" }
        };
    }

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "clubsByCategory")
    public static Object[][] getClubsByCategory() {
        // Query to find clubs by category to which they belong to in the DB
        return new Object[][] {
                { ClubRepository.getJeromeITSchool(),
                 "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN club_category as cc ON c.id=cc.club_id\n" +
                        "INNER JOIN categories as cs ON cc.category_id=cs.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND cs.name = '" + ClubRepository.getJeromeITSchool().getCategory() + "';\n" }
        };
    }

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "clubsByCenter")
    public static Object[][] getClubsByCenter() {
        final String CENTER_NAME = "Комунальний позашкільний навчальний заклад \"Одеський будинок дитячої та юнацької творчості \"Тоніка\"";
        // Query to find clubs by center to which they belong to in the DB
        return new Object[][] {
                { CENTER_NAME, "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "INNER JOIN centers as cn ON c.center_id=cn.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND cn.name = '" + CENTER_NAME + "';" }
        };
    }

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "clubByTitle")
    public static Object[][] getClubByTitle() {
        // Query to find club by its name in the DB
        return new Object[][] {
                { ClubRepository.getGreenCountry(), "SELECT DISTINCT c.name\n" +
                        "FROM clubs as c\n" +
                        "INNER JOIN locations as l ON c.id=l.club_id\n" +
                        "INNER JOIN cities as ct ON l.city_id=ct.id\n" +
                        "WHERE ct.name = '" + KYIV_CITY + "'\n" +
                        "AND c.name = '" + ClubRepository.getGreenCountry().getTitle() + "';" }
        };
    }

}
