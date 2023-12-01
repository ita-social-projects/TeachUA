import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import EditClubPage from "../PO/EditClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import BasePage from "../PO/BasePage";
import ClubInfoPage from "../PO/ClubInfoPage";
import {EDIT_LOCATION_DETAILS, EDIT_CLUB_DETAILS, EDIT_CLUB_CONTACT_DETAILS, CLUB_CATEGORIES} from "../constants/clubInformation.constants";
import {CREATE_CLUB_REQUEST } from "../constants/api.constants";

let apiservice, editclubpage, homepage, userpage, basepage, clubinfopage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        editclubpage = new EditClubPage(page);
        basepage = new BasePage(page);
        userpage = new UserPage(page);
        homepage = new HomePage(page);

        await apiservice.apiLoginAs('admin');
        await homepage.gotoHomepage();
        await userpage.gotoUserPage();
    })

    test("Verify that an existing club can be succesfully edited and the details will be correct", async ({ page }) => {
        try {
            await apiservice.deleteClubByTitle(EDIT_CLUB_DETAILS.clubTitle);
            await apiservice.deleteClubByTitle(CREATE_CLUB_REQUEST.body.name);
        } catch (e) {
            console.error(e);
        }
        await apiservice.createNewClub(CREATE_CLUB_REQUEST.body.name);

        await userpage.openClubEditModeByTitle(CREATE_CLUB_REQUEST.body.name);
        await basepage.fillInputField(editclubpage.clubNameField, EDIT_CLUB_DETAILS.clubTitle);
        
        await editclubpage.setAcceptableChildAge(EDIT_CLUB_DETAILS.clubEnterAge, EDIT_CLUB_DETAILS.clubClosingAge);
        await editclubpage.selectRelatedCenter(EDIT_CLUB_DETAILS.clubRelatedCenter);
        await editclubpage.proceedToNextStep();

        await editclubpage.openEditLocationWindow(CREATE_CLUB_REQUEST.body.locations.name);
        await editclubpage.fillInputField(editclubpage.locationNameField, EDIT_LOCATION_DETAILS.locationName)
        await editclubpage.selectLocationCity(EDIT_LOCATION_DETAILS.locationCity);
        await editclubpage.selectLocationDistrict(EDIT_LOCATION_DETAILS.locationDistrict);
        await editclubpage.fillInputField(editclubpage.locationAddressField, EDIT_LOCATION_DETAILS.locationAddress)
        await editclubpage.fillInputField(editclubpage.locationCoordinatesField, EDIT_LOCATION_DETAILS.locationCoordinates)
        await editclubpage.fillInputField(editclubpage.locationPhoneNumberField, EDIT_LOCATION_DETAILS.locationPhone);
        await editclubpage.confirmLocationAddition();

        await editclubpage.verifyLocationPresence(EDIT_LOCATION_DETAILS.locationName)
        
        await editclubpage.fillInputField(editclubpage.clubPhoneNumberField, EDIT_CLUB_CONTACT_DETAILS.phoneNumber);
        await editclubpage.fillInputField(editclubpage.clubFacebookField, EDIT_CLUB_CONTACT_DETAILS.facebook);
        await editclubpage.fillInputField(editclubpage.clubEmailField, EDIT_CLUB_CONTACT_DETAILS.email);
        await editclubpage.fillInputField(editclubpage.clubWhatsUpField, EDIT_CLUB_CONTACT_DETAILS.whatsup);
        await editclubpage.fillInputField(editclubpage.clubSkypeField, EDIT_CLUB_CONTACT_DETAILS.skype);
        
        await editclubpage.proceedToNextStep();

        await editclubpage.fillInputField(editclubpage.descriptionTextArea, EDIT_CLUB_DETAILS.clubDescription);
        await editclubpage.completeClubCreation();

        await basepage.verifyElementVisibility(editclubpage.clubUpdatedSuccessMessage, true);
        
        await userpage.gotoUserPage();

        clubinfopage = new ClubInfoPage(page);
        await userpage.openClubInfoPage(EDIT_CLUB_DETAILS.clubTitle);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageTitle,
            true,
            EDIT_CLUB_DETAILS.clubTitle
        );
        await clubinfopage.expectElementToHaveText(clubinfopage.clubPageTags, [
            CLUB_CATEGORIES.acting,
            CLUB_CATEGORIES.developmentCenter,
            CLUB_CATEGORIES.programming,
        ]);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageAddress,
            true,
            EDIT_LOCATION_DETAILS.locationAddress
        );
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageDescription,
            true,
            EDIT_CLUB_DETAILS.clubDescription
        );
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.email)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.facebook)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, '+38'+EDIT_CLUB_CONTACT_DETAILS.phoneNumber)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.whatsup)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, EDIT_CLUB_CONTACT_DETAILS.skype)
        

    });

    test("Verify that an existing club can be succesfully deleted", async ({ page }) => {
        await apiservice.createNewClub();
        await userpage.removeClubByTitle(CREATE_CLUB_REQUEST.body.name);
        await userpage.gotoUserPage();
        await userpage.verifyElementExistance(userpage.clubsNames, CREATE_CLUB_REQUEST.body.name, false);

    });

    test("Verify that an club's location can be succesfully deleted", async ({ page }) => {
        try {
            await apiservice.deleteClubByTitle(CREATE_CLUB_REQUEST.body.name);
        } catch (e) {
            console.error(e);
        }
        await apiservice.createNewClub(CREATE_CLUB_REQUEST.body.name);
        await userpage.openClubEditModeByTitle(CREATE_CLUB_REQUEST.body.name);
        await editclubpage.proceedToNextStep();
        await editclubpage.deleteLocation(CREATE_CLUB_REQUEST.body.locations.name);
        await editclubpage.verifyLocationPresence(CREATE_CLUB_REQUEST.body.locations.name, false);
        await editclubpage.proceedToNextStep();
        await editclubpage.completeClubCreation();
        await userpage.gotoUserPage();
        clubinfopage = new ClubInfoPage(page);
        await userpage.openClubInfoPage(CREATE_CLUB_REQUEST.body.name);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageAddress,
            true,
            EDIT_LOCATION_DETAILS.locationOnline
        );
    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
