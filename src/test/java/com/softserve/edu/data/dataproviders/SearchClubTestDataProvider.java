package com.softserve.edu.data.dataproviders;

import com.softserve.edu.data.club.ClubRepository;
import org.testng.annotations.DataProvider;

public class SearchClubTestDataProvider {

    // DataProvider method is static because it's used in foreign class(in test class from another package)
    @DataProvider(name = "clubData")
    public static Object[][] getClubData() {
        return new Object[][] {
                { ClubRepository.getJeromeITSchool() },                     // get data about Jerome IT School club
        };
    }

}
