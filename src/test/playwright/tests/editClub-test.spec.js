import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import EditClubPage from "../PO/EditClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import BasePage from "../PO/BasePage";
import {editLocationDetails, editClubDetails, editClubContactDetails, newClubCorrectDetails} from "../constants/clubInformation.constants";
import {createClubRequest } from "../constants/api.constants";

let apiservice, editclubpage, homepage, userpage, basepage;

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

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        await apiservice.createNewClub(createClubRequest.body.name);
        try {
            await apiservice.deleteClubByTitle(editClubDetails.CLUB_TITLE);
        } catch (e) {
            console.error(e);
        }

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

        await editclubpage.verifyLocationListContainsItem(editLocationDetails.LOCATION_NAME)
        await editclubpage.verifyLocationListContainsItem(editLocationDetails.LOCATION_ADDRESS)
        
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

    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
