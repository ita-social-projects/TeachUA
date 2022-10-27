package com.softserve.edu.test.ui;

import com.softserve.edu.pages.guest.aboutus.AboutUsPage;
import com.softserve.edu.pages.guest.challenge.ChallengePage;
import com.softserve.edu.pages.guest.clubs.ClubsPage;
import com.softserve.edu.pages.guest.home.HomePage;
import com.softserve.edu.pages.guest.news.NewsPage;
import com.softserve.edu.pages.guest.servicesinukrainian.ServicesInUkrainianPage;
import com.softserve.edu.testcases.BaseTestSetup;
import org.testng.annotations.Test;

public class BaseTest extends BaseTestSetup {

    @Test
    public void linksImagesTest() {
        logger.info("Test if links and images on the page are not broken started");

        // Follow the link and load application
        HomePage homePage = loadApplication();

        // Check if all the links and images that are present on Home page are not broken
        homePage.verifyPageLinksImages();

        // Go to clubs page
        ClubsPage clubsPage = homePage.gotoClubsPage();

        // Check if all the links and images that are present on Clubs page are not broken
        clubsPage.verifyPageLinksImages();

        // Open challenge page
        ChallengePage challengePage = clubsPage.openChallengePage();

        // Check if all the links and images that are present on Challenge page are not broken
        challengePage.verifyPageLinksImages();

        // Go to news page
        NewsPage newsPage = clubsPage.gotoNewsPage();

        // Check if all the links and images that are present on News page are not broken
        newsPage.verifyPageLinksImages();

        // Go to About us page
        AboutUsPage aboutUsPage = newsPage.gotoAboutUsPage();

        // Check if all the links and images that are present on About us page are not broken
        aboutUsPage.verifyPageLinksImages();

        // Go to Services in ukrainian page
        ServicesInUkrainianPage servicesInUkrainianPage = aboutUsPage.gotoServicesInUkrainianPage();

        // Check if all the links and images that are present on Services in ukrainian page are not broken
        servicesInUkrainianPage.verifyPageLinksImages();

        logger.info("Test if links and images on the page are not broken finished");
    }

}
