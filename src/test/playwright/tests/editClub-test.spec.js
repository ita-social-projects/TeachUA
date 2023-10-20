import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import EditClubPage from "../PO/EditClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import BasePage from "../PO/BasePage";
import ClubInfoPage from "../PO/ClubInfoPage";
import {editLocationDetails, editClubDetails, editClubContactDetails, clubCategories} from "../constants/clubInformation.constants";
import {createClubRequest } from "../constants/api.constants";

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
            await apiservice.deleteClubByTitle(editClubDetails.CLUB_TITLE);
            await apiservice.deleteClubByTitle(createClubRequest.body.name);
        } catch (e) {
            console.error(e);
        }
        await apiservice.createNewClub(createClubRequest.body.name);

        await userpage.openClubEditModeByTitle(createClubRequest.body.name);
        await basepage.fillInputField(editclubpage.clubNameField, editClubDetails.CLUB_TITLE);
        
        await editclubpage.setAcceptableChildAge(editClubDetails.CLUB_ENTER_AGE, editClubDetails.CLUB_CLOSING_AGE);
        await editclubpage.selectRelatedCenter(editClubDetails.CLUB_RELATED_CENTER);
        await editclubpage.proceedToNextStep();

        await editclubpage.openEditLocationWindow(createClubRequest.body.locations.name);
        await editclubpage.fillInputField(editclubpage.locationNameField, editLocationDetails.LOCATION_NAME)
        await editclubpage.selectLocationCity(editLocationDetails.LOCATION_CITY);
        await editclubpage.selectLocationDistrict(editLocationDetails.LOCATION_DISTRICT);
        await editclubpage.fillInputField(editclubpage.locationAddressField, editLocationDetails.LOCATION_ADDRESS)
        await editclubpage.fillInputField(editclubpage.locationCoordinatesField, editLocationDetails.LOCATION_COORDINATES)
        await editclubpage.fillInputField(editclubpage.locationPhoneNumberField, editLocationDetails.LOCATION_PHONE);
        await editclubpage.confirmLocationAddition();

        await editclubpage.verifyLocationPresence(editLocationDetails.LOCATION_NAME)
        
        await editclubpage.fillInputField(editclubpage.clubPhoneNumberField, editClubContactDetails.PHONE_NUMBER);
        await editclubpage.fillInputField(editclubpage.clubFacebookField, editClubContactDetails.FACEBOOK);
        await editclubpage.fillInputField(editclubpage.clubEmailField, editClubContactDetails.EMAIL);
        await editclubpage.fillInputField(editclubpage.clubWhatsUpField, editClubContactDetails.WHATSUP);
        await editclubpage.fillInputField(editclubpage.clubSkypeField, editClubContactDetails.SKYPE);
        
        await editclubpage.proceedToNextStep();

        await editclubpage.fillInputField(editclubpage.descriptionTextArea, editClubDetails.CLUB_DESCRIPTION);
        await editclubpage.completeClubCreation();

        await basepage.verifyElementVisibility(editclubpage.clubUpdatedSuccessMessage, true);
        
        await userpage.gotoUserPage();

        clubinfopage = new ClubInfoPage(page);
        await userpage.openClubInfoPage(editClubDetails.CLUB_TITLE);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageTitle,
            true,
            editClubDetails.CLUB_TITLE
        );
        await clubinfopage.expectElementToHaveText(clubinfopage.clubPageTags, [
            clubCategories.ACTING,
            clubCategories.DEVELOPMENT_CENTER,
            clubCategories.PROGRAMMING,
        ]);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageAddress,
            true,
            editLocationDetails.LOCATION_ADDRESS
        );
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageDescription,
            true,
            editClubDetails.CLUB_DESCRIPTION
        );
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, editClubContactDetails.EMAIL)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, editClubContactDetails.FACEBOOK)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, '+38'+editClubContactDetails.PHONE_NUMBER)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, editClubContactDetails.WHATSUP)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, editClubContactDetails.SKYPE)
        

    });

    test("Verify that an existing club can be succesfully deleted", async ({ page }) => {
        await apiservice.createNewClub(createClubRequest.body.name);
        await userpage.removeClubByTitle(createClubRequest.body.name);
        await userpage.gotoUserPage();
        await userpage.verifyElementExistance(userpage.clubsNames, createClubRequest.body.name, false);

    });

    test("Verify that an club's location can be succesfully deleted", async ({ page }) => {
        try {
            await apiservice.deleteClubByTitle(createClubRequest.body.name);
        } catch (e) {
            console.error(e);
        }
        await apiservice.createNewClub(createClubRequest.body.name);
        await userpage.openClubEditModeByTitle(createClubRequest.body.name);
        await editclubpage.proceedToNextStep();
        await editclubpage.deleteLocation(createClubRequest.body.locations.name);
        await editclubpage.verifyLocationPresence(createClubRequest.body.locations.name, false);
        await editclubpage.proceedToNextStep();
        await editclubpage.completeClubCreation();
        await userpage.gotoUserPage();
        clubinfopage = new ClubInfoPage(page);
        await userpage.openClubInfoPage(createClubRequest.body.name);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageAddress,
            true,
            editLocationDetails.LOCATION_ONLINE
        );
    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
