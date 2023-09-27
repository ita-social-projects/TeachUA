import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import AddClubPage from "../PO/AddClubPage";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import {clubCategories, newLocationCorrectDetails, newClubCorrectContactDetails, newClubIncorrectContactDetails, newClubCorrectDetails, newClubIncorrectDetails, newLocationIncorrectDetails} from "../constants/clubInformation.constants";
import {addClubValidationErrorMessages,addLocationValidationErrorMessages} from "../constants/messages.constants";

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
        await addclubpage.fillInputField(addclubpage.clubNameField, newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.toggleStatedCheckboxes(
            clubCategories.ACTING,
            clubCategories.DEVELOPMENT_CENTER,
            clubCategories.PROGRAMMING
        );
        await addclubpage.setAcceptableChildAge(newClubCorrectDetails.CLUB_ENTER_AGE, newClubCorrectDetails.CLUB_CLOSING_AGE);
        await addclubpage.selectRelatedCenter(newClubCorrectDetails.CLUB_RELATED_CENTER);
        await addclubpage.proceedToNextStep();


        await addclubpage.openAddLocationWindow();
        await addclubpage.fillInputField(addclubpage.locationNameField, newLocationCorrectDetails.LOCATION_NAME)
        await addclubpage.selectLocationCity(newLocationCorrectDetails.LOCATION_CITY);
        await addclubpage.selectLocationDistrict(newLocationCorrectDetails.LOCATION_DISTRICT);
        await addclubpage.selectLocationStation(newLocationCorrectDetails.LOCATION_STATION);
        await addclubpage.fillInputField(addclubpage.locationAddressField, newLocationCorrectDetails.LOCATION_ADDRESS)
        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, newLocationCorrectDetails.LOCATION_COORDINATES)
        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, newLocationCorrectDetails.LOCATION_PHONE);
        await addclubpage.confirmLocationAddition();

        await addclubpage.verifyLocationListContainsItem(newLocationCorrectDetails.LOCATION_NAME)
        await addclubpage.verifyLocationListContainsItem(newLocationCorrectDetails.LOCATION_ADDRESS)
        await addclubpage.verifyAvailableOnlineTooltipAppearsOnHover();
        
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, newClubCorrectContactDetails.PHONE_NUMBER);
        await addclubpage.fillInputField(addclubpage.clubFacebookField, newClubCorrectContactDetails.FACEBOOK);
        await addclubpage.fillInputField(addclubpage.clubEmailField, newClubCorrectContactDetails.EMAIL);
        await addclubpage.fillInputField(addclubpage.clubWhatsUpField, newClubCorrectContactDetails.WHATSUP);
        await addclubpage.fillInputField(addclubpage.clubWebPageField, newClubCorrectContactDetails.WEB_PAGE);
        
        await addclubpage.proceedToNextStep();

        await addclubpage.uploadLogo();
        await addclubpage.uploadCover();
        await addclubpage.uploadPhotoes();

        await addclubpage.fillInputField(addclubpage.descriptionTextArea, newClubCorrectDetails.CLUB_DESCRIPTION);
        await addclubpage.completeClubCreation();

        await addclubpage.verifyClubCreationSuccessMessageDisplayed();
        
        await userpage.gotoUserPage();
        await userpage.verifyClubExistsByTitle(newClubCorrectDetails.clubTitle);  
    });

    test("Verify validation error messages appearence/disappearence on the first step", async ({ page }) => {
        await addclubpage.proceedToNextStep();
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, true, addClubValidationErrorMessages.NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.categoryFieldErrorMessage, true,addClubValidationErrorMessages.CATEGORY_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, true,addClubValidationErrorMessages.AGE_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageToFieldErrorMessage, true,addClubValidationErrorMessages.AGE_IS_MANDATORY);

        await addclubpage.fillInputField(addclubpage.clubNameField, newClubIncorrectDetails.CLUB_TITLE);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, true, addClubValidationErrorMessages.NAME_IS_INCORRECT);
        await addclubpage.fillInputField(addclubpage.clubNameField, newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, false);

        await addclubpage.toggleStatedCheckboxes(clubCategories.ACTING);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.categoryFieldErrorMessage, false);

        await addclubpage.setAcceptableChildAge(
            newClubCorrectDetails.CLUB_ENTER_AGE,
            newClubCorrectDetails.CLUB_CLOSING_AGE
        );
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, false);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, false);
    });

    test("Verify validation error messages appearence/disappearence on the second step", async ({ page }) => {
        await addclubpage.fillInputField(addclubpage.clubNameField, newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.toggleStatedCheckboxes(
            clubCategories.ACTING,
        );
        await addclubpage.setAcceptableChildAge(newClubCorrectDetails.CLUB_ENTER_AGE, newClubCorrectDetails.CLUB_CLOSING_AGE);
        await addclubpage.proceedToNextStep();
        await addclubpage.openAddLocationWindow();

        //trigget validation of mandatory fields and verify that each is present and correct
        //message is displayed
        await addclubpage.confirmLocationAddition();
        
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, true, addLocationValidationErrorMessages.CITY_IS_MANDATORY);

        //this one is ommitted because of the defect which displays both mandatory and incorrect value validation for the address field
        //await addclubpage.verifyElementVisibilityAndText(addclubpage.locationAddressFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_ARE_INCORRECT);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, true, addLocationValidationErrorMessages.PHONE_IS_MANDATORY);

        //fill fields with incorrect data and verify that appropriate error message appears
        await addclubpage.fillInputField(addclubpage.locationNameField, newLocationIncorrectDetails.LOCATION_NAME);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_INCORRECT);

        await addclubpage.fillInputField(addclubpage.locationAddressField, newLocationIncorrectDetails.LOCATION_ADDRESS);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationAddressFieldErrorMessage, true, addLocationValidationErrorMessages.ADDRESS_IS_INCORRECT);

        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, newLocationIncorrectDetails.LOCATION_COORDINATES);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_ARE_INCORRECT);
        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, newLocationIncorrectDetails.LOCATION_COORDINATES_WITH_LETTERS);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_CANT_HAVE_LETTERS);

        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, newLocationIncorrectDetails.LOCATION_PHONE);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, true, addLocationValidationErrorMessages.PHONE_IS_INCORRECT);

        //fill fields with correct data and verify that error messages disappear
        await addclubpage.fillInputField(addclubpage.locationNameField, newLocationCorrectDetails.LOCATION_NAME);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, false);

        await addclubpage.selectLocationCity(newLocationCorrectDetails.LOCATION_CITY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationAddressField, newLocationCorrectDetails.LOCATION_ADDRESS);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, newLocationCorrectDetails.LOCATION_COORDINATES);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, newLocationCorrectDetails.LOCATION_PHONE);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, false);

        await addclubpage.confirmLocationAddition();

        //trigger step 2 mandatory fields validation
        await addclubpage.proceedToNextStep();
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_MANDATORY);

        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, newClubIncorrectContactDetails.PHONE_NUMBER_INCORRECT_FORMAT);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_INCORRECT);
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, newClubIncorrectContactDetails.PHONE_NUMBER_INCORRECT_FORMAT_WITH_LETTERS);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_INCORRECT_CANT_HAVE_LETTERS);
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, newClubCorrectContactDetails.PHONE_NUMBER);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.clubEmailField, newClubIncorrectContactDetails.EMAIL);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubEmailFieldErrorMessage, true, addClubValidationErrorMessages.EMAIL_IS_INCORRECT); 
        await addclubpage.fillInputField(addclubpage.clubEmailField, newClubCorrectContactDetails.EMAIL);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubEmailFieldErrorMessage, false); 



    });


    test("Verify validation error messages appearence/disappearence on the third step", async ({ page }) => {
        //await addclubpage.fillClubNameField(newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.fillInputField(addclubpage.clubNameField, newClubCorrectDetails.CLUB_TITLE);
        await addclubpage.toggleStatedCheckboxes(
            clubCategories.ACTING,
        );
        await addclubpage.setAcceptableChildAge(newClubCorrectDetails.CLUB_ENTER_AGE, newClubCorrectDetails.CLUB_CLOSING_AGE);
        await addclubpage.proceedToNextStep();
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, newClubCorrectContactDetails.PHONE_NUMBER);
        await addclubpage.proceedToNextStep();

        await addclubpage.completeClubCreation();
        //to be updated once the correct error messages are implemented
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, true, addClubValidationErrorMessages.DESCRIPTION_IS_INCORRECT); 
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, newClubIncorrectDetails.CLUB_DESCRIPTION);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, true, addClubValidationErrorMessages.DESCRIPTION_IS_INCORRECT_CHAR_LIMIT); 
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, newClubCorrectDetails.CLUB_DESCRIPTION);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, false); 


    });

    test.afterEach(async({page})=>{
        //await page.close();
    })
