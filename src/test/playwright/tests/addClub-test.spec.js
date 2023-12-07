import {test} from "@playwright/test";
import ApiService from "../services/ApiService";
import AddClubPage from "../PO/AddClubPage";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import ClubInfoPage from "../PO/ClubInfoPage";
import {CLUB_CATEGORIES, NEW_LOCATION_CORRECT_DETAILS, NEW_CLUB_CORRECT_CONTACT_DETAILS, NEW_CLUB_INCORRECT_CONTACT_DETAILS, NEW_CLUB_CORRECT_DETAILS, NEW_CLUB_INCORRECT_DETAILS, NEW_LOCATION_INCORRECT_DETAILS} from "../constants/clubInformation.constants";
import {ADD_CLUB_VALIDATION_ERROR_MESSAGE,ADD_LOCATION_VALIDATION_ERROR_MESSAGE, SUCCESS_CLUB_CREATION_MESSAGE} from "../constants/messages.constants";

let apiService, addClubPage, homePage, userPage, clubInfoPage;

    test.beforeEach(async({page})=>{
        apiService = new ApiService(page);
        addClubPage = new AddClubPage(page);
        homePage = new HomePage(page);

        await apiService.apiLoginAs('admin');
        await homePage.openAddClubPage();
    })

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        userPage = new UserPage(page);
        // Remove the club with the test club_title if it exists
        try {
            await apiService.deleteClubByTitle(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        } catch (e) {
            console.error(e);
        }
        
        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
            CLUB_CATEGORIES.developmentCenter,
            CLUB_CATEGORIES.programming
        );
        await addClubPage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addClubPage.selectRelatedCenter(NEW_CLUB_CORRECT_DETAILS.clubRelatedCenter);
        await addClubPage.proceedToNextStep();


        await addClubPage.openAddLocationWindow();
        await addClubPage.fillInputField(addClubPage.locationNameField, NEW_LOCATION_CORRECT_DETAILS.locationName)
        await addClubPage.selectLocationCity(NEW_LOCATION_CORRECT_DETAILS.locationCity);
        await addClubPage.selectLocationDistrict(NEW_LOCATION_CORRECT_DETAILS.locationDistrict);
        await addClubPage.selectLocationStation(NEW_LOCATION_CORRECT_DETAILS.locationStation);
        await addClubPage.fillInputField(addClubPage.locationAddressField, NEW_LOCATION_CORRECT_DETAILS.locationAddress)
        await addClubPage.fillInputField(addClubPage.locationCoordinatesField, NEW_LOCATION_CORRECT_DETAILS.locationCoordinates)
        await addClubPage.fillInputField(addClubPage.locationPhoneNumberField, NEW_LOCATION_CORRECT_DETAILS.locationPhone);
        await addClubPage.confirmLocationAddition();

        await addClubPage.verifyLocationPresence(NEW_LOCATION_CORRECT_DETAILS.locationName);
        
        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addClubPage.fillInputField(addClubPage.clubFacebookField, NEW_CLUB_CORRECT_CONTACT_DETAILS.facebook);
        await addClubPage.fillInputField(addClubPage.clubEmailField, NEW_CLUB_CORRECT_CONTACT_DETAILS.email);
        await addClubPage.fillInputField(addClubPage.clubWhatsUpField, NEW_CLUB_CORRECT_CONTACT_DETAILS.whatsup);
        await addClubPage.fillInputField(addClubPage.clubWebPageField, NEW_CLUB_CORRECT_CONTACT_DETAILS.webPage);
        
        await addClubPage.proceedToNextStep();

        await addClubPage.uploadLogo();
        await addClubPage.uploadCover();
        await addClubPage.uploadPhotoes();

        await addClubPage.fillInputField(addClubPage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);
        await addClubPage.completeClubCreation();

        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubCreatedSuccessMessage, true, SUCCESS_CLUB_CREATION_MESSAGE);
        
        await userPage.gotoUserPage();

        // Verify that the created club information is correct
        clubInfoPage = new ClubInfoPage(page);
        await userPage.gotoUserPage();
        await userPage.openClubInfoPage(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await clubInfoPage.verifyElementVisibilityAndText(
            clubInfoPage.clubPageTitle,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubTitle
        );
        await clubInfoPage.expectElementToHaveText(clubInfoPage.clubPageTags, [
            CLUB_CATEGORIES.acting,
            CLUB_CATEGORIES.developmentCenter,
            CLUB_CATEGORIES.programming,
        ]);
        await clubInfoPage.verifyElementVisibilityAndText(
            clubInfoPage.clubPageAddress,
            true,
            NEW_LOCATION_CORRECT_DETAILS.locationAddress
        );
        await clubInfoPage.verifyElementVisibilityAndText(
            clubInfoPage.clubPageDescription,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubDescription
        );
        await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.email)
        await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.facebook)
        await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, '+38'+NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber)
        await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.whatsup)
        await clubInfoPage.expectElementToContainText(clubInfoPage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.webPage)
        
        await clubInfoPage.verifyElementVisibilityAndText(
            clubInfoPage.clubPageDevelopmentCenter,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubRelatedCenter
        );
    });
    

    test("Verify validation error messages appearence/disappearence on the first step", async ({ page }) => {
        await addClubPage.proceedToNextStep();
        await addClubPage.verifyElementVisibilityAndText(addClubPage.nameFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.nameIsMandatory);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.categoryFieldErrorMessage, true,ADD_CLUB_VALIDATION_ERROR_MESSAGE.categoryIsMandatory);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.ageFromFieldErrorMessage, true,ADD_CLUB_VALIDATION_ERROR_MESSAGE.ageIsMandatory);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.ageToFieldErrorMessage, true,ADD_CLUB_VALIDATION_ERROR_MESSAGE.ageIsMandatory);

        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_INCORRECT_DETAILS.clubTitle);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.nameFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.nameIsIncorrect);
        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.nameFieldErrorMessage, false);

        await addClubPage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.categoryFieldErrorMessage, false);

        await addClubPage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addClubPage.verifyElementVisibilityAndText(addClubPage.ageFromFieldErrorMessage, false);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.ageFromFieldErrorMessage, false);
    });

    test("Verify validation error messages appearence/disappearence on the second step", async ({ page }) => {
        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
        );
        await addClubPage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addClubPage.proceedToNextStep();
        await addClubPage.openAddLocationWindow();

        // Trigger validation of mandatory fields and verify that each is present and correct
        await addClubPage.confirmLocationAddition();
        
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationNameFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.locationNameIsMandatory);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCityErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.cityIsMandatory);

        // This one is ommitted because of the defect which displays both mandatory and incorrect value validation for the address field
        // await addclubpage.verifyElementVisibilityAndText(addclubpage.locationAddressFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.locationNameIsMandatory);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCoordinatesFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.coordinatesAreIncorrect);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationPhoneFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.phoneIsMandatory);

        // Fill fields with incorrect data and verify that appropriate error message appears
        await addClubPage.fillInputField(addClubPage.locationNameField, NEW_LOCATION_INCORRECT_DETAILS.locationName);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationNameFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.locationNameIsIncorrect);

        await addClubPage.fillInputField(addClubPage.locationAddressField, NEW_LOCATION_INCORRECT_DETAILS.locationAddress);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationAddressFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.addressIsIncorrect);

        await addClubPage.fillInputField(addClubPage.locationCoordinatesField, NEW_LOCATION_INCORRECT_DETAILS.locationCoordinates);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCoordinatesFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.coordinatesAreIncorrect);
        await addClubPage.fillInputField(addClubPage.locationCoordinatesField, NEW_LOCATION_INCORRECT_DETAILS.locationCoordinatesWithLetters);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCoordinatesFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.coordinatesCantHaveLetters);

        await addClubPage.fillInputField(addClubPage.locationPhoneNumberField, NEW_LOCATION_INCORRECT_DETAILS.locationPhone);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationPhoneFieldErrorMessage, true, ADD_LOCATION_VALIDATION_ERROR_MESSAGE.phoneIsIncorrect);

        // Fill fields with correct data and verify that error messages disappear
        await addClubPage.fillInputField(addClubPage.locationNameField, NEW_LOCATION_CORRECT_DETAILS.locationName);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationNameFieldErrorMessage, false);

        await addClubPage.selectLocationCity(NEW_LOCATION_CORRECT_DETAILS.locationCity);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCityErrorMessage, false);

        await addClubPage.fillInputField(addClubPage.locationAddressField, NEW_LOCATION_CORRECT_DETAILS.locationAddress);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCityErrorMessage, false);

        await addClubPage.fillInputField(addClubPage.locationCoordinatesField, NEW_LOCATION_CORRECT_DETAILS.locationCoordinates);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationCoordinatesFieldErrorMessage, false);

        await addClubPage.fillInputField(addClubPage.locationPhoneNumberField, NEW_LOCATION_CORRECT_DETAILS.locationPhone);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.locationPhoneFieldErrorMessage, false);

        await addClubPage.confirmLocationAddition();

        // Trigger step 2 mandatory fields validation
        await addClubPage.proceedToNextStep();
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubPhoneFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.phoneIsMandatory);

        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.phoneNumberIncorrectFormat);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubPhoneFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.phoneIsIncorrect);
        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.phoneNumberIncorrectFormatWithLetters);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubPhoneFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.phoneIsIncorrectCantHaveLetters);
        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubPhoneFieldErrorMessage, false);

        await addClubPage.fillInputField(addClubPage.clubEmailField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.email);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubEmailFieldErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.emailIsIncorrect); 
        await addClubPage.fillInputField(addClubPage.clubEmailField, NEW_CLUB_CORRECT_CONTACT_DETAILS.email);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubEmailFieldErrorMessage, false); 
    });


    test("Verify validation error messages appearence/disappearence on the third step", async ({ page }) => {
        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
        );
        await addClubPage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addClubPage.proceedToNextStep();
        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addClubPage.proceedToNextStep();

        await addClubPage.completeClubCreation();
        // To be updated once the correct error messages are implemented
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubDescriptionErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.descriptionIsIncorrect); 
        await addClubPage.fillInputField(addClubPage.descriptionTextArea, NEW_CLUB_INCORRECT_DETAILS.clubDescription);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubDescriptionErrorMessage, true, ADD_CLUB_VALIDATION_ERROR_MESSAGE.descriptionIsIncorrectCharLimit); 
        await addClubPage.fillInputField(addClubPage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);
        await addClubPage.verifyElementVisibilityAndText(addClubPage.clubDescriptionErrorMessage, false); 

    });


    test("Verify that tooltips and warning messages appear", async ({ page }) => {
        // Remove the club with the test club_title if it exists
        try {
            await apiService.deleteClubByTitle(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        } catch (e) {
            console.error(e);
        }

        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addClubPage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addClubPage.proceedToNextStep();
        await addClubPage.openAddLocationWindow();

        await addClubPage.verifyLocationNameTooltipAppearsOnHover();
        await addClubPage.verifyLocationPhoneTooltipAppearsOnHover();
        await addClubPage.closeAddLocationWindow();

        await addClubPage.verifyAvailableOnlineTooltipAppearsOnHover();

        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addClubPage.proceedToNextStep();

        await addClubPage.verifyElementVisibility(addClubPage.clubIsAutomaticallyOnlineMessage, true);
        await addClubPage.fillInputField(addClubPage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);

        /* 
         * Complete club creation and go through the creation steps again to verify that
         * 'this club already exists' message appears
         */
        await addClubPage.completeClubCreation();
        await homePage.openAddClubPage();
        await addClubPage.fillInputField(addClubPage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addClubPage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addClubPage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addClubPage.proceedToNextStep();
        await addClubPage.fillInputField(addClubPage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addClubPage.proceedToNextStep();
        await addClubPage.fillInputField(addClubPage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);

        await addClubPage.completeClubCreation();
        await addClubPage.verifyElementVisibility(addClubPage.clubAlreadyExistMessage, true);
    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
