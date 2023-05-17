package com.softserve.teachua.tests;

import com.softserve.teachua.pages.HomePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HomeTest extends TestRunner {

    @Test
    public void checkHome() {
        // Steps
        HomePage homePage = loadApplication();
        presentationSleep();
        System.out.println("***homePage.getDetailsButtonText(): " + homePage.getDetailsButtonText());
        //
        // Check
        //Assertions.assertEquals(HomePage.DETAILS_BUTTON_TEXT, homePage.getDetailsButtonText());
        Assertions.assertEquals(HomePage.CLUB_DIRECTION_TEXT, homePage.getClubDirectionLabelText());
        //
        presentationSleep();
        System.out.println("HomeTest checkHome() done"); // Use Logging
    }
}
