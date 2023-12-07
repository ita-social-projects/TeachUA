import { test, chromium } from "@playwright/test";
import ApiService from "../services/ApiService";
import EditClubPage from "../PO/EditClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import BasePage from "../PO/BasePage";
import ClubInfoPage from "../PO/ClubInfoPage";
import {
    EDIT_LOCATION_DETAILS,
    EDIT_CLUB_DETAILS,
    EDIT_CLUB_CONTACT_DETAILS,
    CLUB_CATEGORIES,
} from "../constants/clubInformation.constants";
import { CREATE_CLUB_REQUEST_2 } from "../constants/api.constants";
import { USER_ROLES } from "../constants/general.constants";

let page, apiService, editClubPage, homePage, userPage, basePage, clubInfoPage;

test.beforeAll(async () => {
    const browser = await chromium.launch();
    const context = await browser.newContext();
    page = await context.newPage();
    apiService = new ApiService(page);
    basePage = new BasePage(page);
    userPage = new UserPage(page);
    editClubPage = new EditClubPage(page);
    clubInfoPage = new ClubInfoPage(page);
    homePage = new HomePage(page);
    await apiService.apiLoginAs(USER_ROLES.admin);
});

test.beforeEach(async () => {
    await homePage.gotoHomepage();
    await userPage.gotoUserPage();
    await apiService.deleteClubByTitle(EDIT_CLUB_DETAILS.clubTitle);
    await apiService.deleteClubByTitle(CREATE_CLUB_REQUEST_2.body.name);
    await apiService.createNewClub(CREATE_CLUB_REQUEST_2);
});

test("Verify that an existing club can be succesfully edited and the details will be correct", async () => {
    await userPage.openClubEditModeByTitle(CREATE_CLUB_REQUEST_2.body.name);
    await basePage.fillInputField(editClubPage.clubNameField, EDIT_CLUB_DETAILS.clubTitle);

    await editClubPage.setAcceptableChildAge(EDIT_CLUB_DETAILS.clubEnterAge, EDIT_CLUB_DETAILS.clubClosingAge);
    await editClubPage.selectRelatedCenter(EDIT_CLUB_DETAILS.clubRelatedCenter);
    await editClubPage.proceedToNextStep();

    await editClubPage.openEditLocationWindow(CREATE_CLUB_REQUEST_2.body.locations.name);
    await editClubPage.fillInputField(editClubPage.locationNameField, EDIT_LOCATION_DETAILS.locationName);
    await editClubPage.selectLocationCity(EDIT_LOCATION_DETAILS.locationCity);
    await editClubPage.selectLocationDistrict(EDIT_LOCATION_DETAILS.locationDistrict);
    await editClubPage.fillInputField(editClubPage.locationAddressField, EDIT_LOCATION_DETAILS.locationAddress);
    await editClubPage.fillInputField(editClubPage.locationCoordinatesField, EDIT_LOCATION_DETAILS.locationCoordinates);
    await editClubPage.fillInputField(editClubPage.locationPhoneNumberField, EDIT_LOCATION_DETAILS.locationPhone);
    await editClubPage.confirmLocationAddition();

    await editClubPage.verifyLocationPresence(EDIT_LOCATION_DETAILS.locationName);

    await editClubPage.fillInputField(editClubPage.clubPhoneNumberField, EDIT_CLUB_CONTACT_DETAILS.phoneNumber);
    await editClubPage.fillInputField(editClubPage.clubFacebookField, EDIT_CLUB_CONTACT_DETAILS.facebook);
    await editClubPage.fillInputField(editClubPage.clubEmailField, EDIT_CLUB_CONTACT_DETAILS.email);
    await editClubPage.fillInputField(editClubPage.clubWhatsUpField, EDIT_CLUB_CONTACT_DETAILS.whatsup);
    await editClubPage.fillInputField(editClubPage.clubSkypeField, EDIT_CLUB_CONTACT_DETAILS.skype);

    await editClubPage.proceedToNextStep();

    await editClubPage.fillInputField(editClubPage.descriptionTextArea, EDIT_CLUB_DETAILS.clubDescription);
    await editClubPage.completeClubCreation();

    await basePage.verifyElementVisibility(editClubPage.clubUpdatedSuccessMessage, true);

    await userPage.gotoUserPage();

    await userPage.openClubInfoPage(EDIT_CLUB_DETAILS.clubTitle);
    await clubInfoPage.verifyElementVisibilityAndText(clubInfoPage.clubPageTitle, true, EDIT_CLUB_DETAILS.clubTitle);
    await clubInfoPage.expectElementToHaveText(clubInfoPage.clubPageTags, [
        CLUB_CATEGORIES.acting,
        CLUB_CATEGORIES.developmentCenter,
        CLUB_CATEGORIES.programming,
    ]);
    await clubInfoPage.verifyElementVisibilityAndText(
        clubInfoPage.clubPageAddress,
        true,
        EDIT_LOCATION_DETAILS.locationAddress
    );
    await clubInfoPage.verifyElementVisibilityAndText(
        clubInfoPage.clubPageDescription,
        true,
        EDIT_CLUB_DETAILS.clubDescription
    );
    await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.email);
    await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.facebook);
    await clubInfoPage.expectElementToContainText(
        clubInfoPage.clubPageContactData,
        EDIT_CLUB_CONTACT_DETAILS.phoneNumber
    );
    await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.whatsup);
    await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.skype);
});

test("Verify that an existing club can be succesfully deleted", async () => {
    await userPage.removeClubByTitle(CREATE_CLUB_REQUEST_2.body.name);
    await userPage.gotoUserPage();
    await userPage.verifyElementExistance(userPage.clubsNames, CREATE_CLUB_REQUEST_2.body.name, false);
});

test("Verify that an club's location can be succesfully deleted", async () => {
    await userPage.openClubEditModeByTitle(CREATE_CLUB_REQUEST_2.body.name);
    await editClubPage.proceedToNextStep();
    await editClubPage.deleteLocation(CREATE_CLUB_REQUEST_2.body.locations.name);
    await editClubPage.verifyLocationPresence(CREATE_CLUB_REQUEST_2.body.locations.name, false);
    await editClubPage.proceedToNextStep();
    await editClubPage.completeClubCreation();
    await userPage.gotoUserPage();
    await userPage.openClubInfoPage(CREATE_CLUB_REQUEST_2.body.name);
    await clubInfoPage.verifyElementVisibilityAndText(
        clubInfoPage.clubPageAddress,
        true,
        EDIT_LOCATION_DETAILS.locationOnline
    );
});

test.afterAll(async () => {
    await page.close();
});
