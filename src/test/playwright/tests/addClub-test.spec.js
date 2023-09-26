import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import AddClubPage from "../PO/AddClubPage";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import {clubCategories, newLocationCorrectDetails, newClubCorrectContactDetails,newClubDescription, newClubCorrectDetails, newClubIncorrectDetails} from "../constants/clubInformation.constants";

let apiservice, addclubpage, homepage, userpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        addclubpage = new AddClubPage(page);
        homepage = new HomePage(page);

        await apiservice.apiLoginAs('admin');
        await homepage.gotoHomepage();
        await addclubpage.gotoAddClubPage();
    })

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        userpage = new UserPage(page);
        await addclubpage.fillClubNameField(newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.toggleStatedCheckboxes(
            clubCategories.ACTING,
            clubCategories.DEVELOPMENT_CENTER,
            clubCategories.PROGRAMMING
        );
        await addclubpage.setAcceptableChildAge(newClubCorrectDetails.CLUB_ENTER_AGE, newClubCorrectDetails.CLUB_CLOSING_AGE);
        await addclubpage.selectRelatedCenter(newClubCorrectDetails.CLUB_RELATED_CENTER);
        await addclubpage.proceedToNextStep();


        await addclubpage.openAddLocationWindow();
        await addclubpage.fillLocationName(newLocationCorrectDetails.LOCATION_NAME)
        await addclubpage.selectLocationCity(newLocationCorrectDetails.LOCATION_CITY);
        await addclubpage.selectLocationDistrict(newLocationCorrectDetails.LOCATION_DISTRICT);
        await addclubpage.selectLocationStation(newLocationCorrectDetails.LOCATION_STATION);
        await addclubpage.fillLocationAddress(newLocationCorrectDetails.LOCATION_ADDRESS)
        await addclubpage.fillLocationCoordinates(newLocationCorrectDetails.LOCATION_COORDINATES)
        await addclubpage.fillLocationPhoneNumber(newLocationCorrectDetails.LOCATION_PHONE);
        await addclubpage.confirmLocationAddition();

        await addclubpage.verifyLocationListContainsItem(newLocationCorrectDetails.LOCATION_NAME)
        await addclubpage.verifyLocationListContainsItem(newLocationCorrectDetails.LOCATION_ADDRESS)
        await addclubpage.verifyAvailableOnlineTooltipAppearsOnHover();
        
        await addclubpage.fillClubPhoneNumberField(newClubCorrectContactDetails.PHONE_NUMBER);
        await addclubpage.fillClubFacebookField(newClubCorrectContactDetails.FACEBOOK);
        await addclubpage.fillClubWhatsUpField(newClubCorrectContactDetails.WHATSUP);
        await addclubpage.fillClubWebPageField(newClubCorrectContactDetails.WEB_PAGE);
        
        await addclubpage.proceedToNextStep();

        await addclubpage.uploadLogo();
        await addclubpage.uploadCover();
        await addclubpage.uploadPhotoes();

        await addclubpage.fillClubDescription(newClubDescription);
        await addclubpage.completeClubCreation();

        await addclubpage.verifyClubCreationSuccessMessageDisplayed();
        
        await userpage.gotoUserPage();
        await userpage.verifyClubExistsByTitle(newClubCorrectDetails.clubTitle);  
    });

    test("Verify validation error messages appearence/disappearence on the first step", async ({ page }) => {
        await addclubpage.proceedToNextStep();
        await addclubpage.verifyDisplayOfClubNameIsMandatoryErrorMessage();
        await addclubpage.verifyDisplayOfClubCategoryIsMandatoryErrorMessage();
        await addclubpage.verifyDisplayOfClubAgeFromIsMandatoryErrorMessage();
        await addclubpage.verifyDisplayOfClubAgeToIsMandatoryErrorMessage();

        await addclubpage.fillClubNameField(newClubIncorrectDetails.CLUB_TITLE);
        await addclubpage.verifyDisplayOfClubNameIsIncorrectErrorMessage(true);
        await addclubpage.fillClubNameField(newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.verifyDisplayOfClubNameIsIncorrectErrorMessage(false);

        await addclubpage.toggleStatedCheckboxes(clubCategories.ACTING);
        await addclubpage.verifyDisplayOfClubCategoryIsMandatoryErrorMessage(false);

        await addclubpage.setAcceptableChildAge(
            newClubCorrectDetails.CLUB_ENTER_AGE,
            newClubCorrectDetails.CLUB_CLOSING_AGE
        );
        await addclubpage.verifyDisplayOfClubAgeFromIsMandatoryErrorMessage(false);
        await addclubpage.verifyDisplayOfClubAgeToIsMandatoryErrorMessage(false);
    });

    test("Verify validation error messages appearence/disappearence on the secon step", async ({ page }) => {
        await addclubpage.fillClubNameField(newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.toggleStatedCheckboxes(
            clubCategories.ACTING,
        );
        await addclubpage.setAcceptableChildAge(newClubCorrectDetails.CLUB_ENTER_AGE, newClubCorrectDetails.CLUB_CLOSING_AGE);
        await addclubpage.proceedToNextStep();
        await addclubpage.openAddLocationWindow();

        await addclubpage.confirmLocationAddition();

    });

    test.afterEach(async({page})=>{
        //await page.close();
    })
