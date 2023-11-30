import {test} from "@playwright/test";
import ApiService from "../services/apiService";
import AddClubPage from "../PO/AddClubPage";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import ClubInfoPage from "../PO/ClubInfoPage";
import {CLUB_CATEGORIES, NEW_LOCATION_CORRECT_DETAILS, NEW_CLUB_CORRECT_CONTACT_DETAILS, NEW_CLUB_INCORRECT_CONTACT_DETAILS, NEW_CLUB_CORRECT_DETAILS, NEW_CLUB_INCORRECT_DETAILS, NEW_LOCATION_INCORRECT_DETAILS} from "../constants/clubInformation.constants";
import {addClubValidationErrorMessages,addLocationValidationErrorMessages, successClubCreationMessage} from "../constants/messages.constants";

let apiservice, addclubpage, homepage, userpage, clubinfopage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        addclubpage = new AddClubPage(page);
        homepage = new HomePage(page);

        await apiservice.apiLoginAs('admin');
        await homepage.openAddClubPage();
    })

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        userpage = new UserPage(page);
        //remove the club with the test club_title if it exists
        try {
            await apiservice.deleteClubByTitle(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        } catch (e) {
            console.error(e);
        }
        
        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
            CLUB_CATEGORIES.developmentCenter,
            CLUB_CATEGORIES.programming
        );
        await addclubpage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addclubpage.selectRelatedCenter(NEW_CLUB_CORRECT_DETAILS.clubRelatedCenter);
        await addclubpage.proceedToNextStep();


        await addclubpage.openAddLocationWindow();
        await addclubpage.fillInputField(addclubpage.locationNameField, NEW_LOCATION_CORRECT_DETAILS.locationName)
        await addclubpage.selectLocationCity(NEW_LOCATION_CORRECT_DETAILS.locationCity);
        await addclubpage.selectLocationDistrict(NEW_LOCATION_CORRECT_DETAILS.locationDistrict);
        await addclubpage.selectLocationStation(NEW_LOCATION_CORRECT_DETAILS.locationStation);
        await addclubpage.fillInputField(addclubpage.locationAddressField, NEW_LOCATION_CORRECT_DETAILS.locationAddress)
        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, NEW_LOCATION_CORRECT_DETAILS.locationCoordinates)
        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, NEW_LOCATION_CORRECT_DETAILS.locationPhone);
        await addclubpage.confirmLocationAddition();

        await addclubpage.verifyLocationPresence(NEW_LOCATION_CORRECT_DETAILS.locationName);
        
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addclubpage.fillInputField(addclubpage.clubFacebookField, NEW_CLUB_CORRECT_CONTACT_DETAILS.facebook);
        await addclubpage.fillInputField(addclubpage.clubEmailField, NEW_CLUB_CORRECT_CONTACT_DETAILS.email);
        await addclubpage.fillInputField(addclubpage.clubWhatsUpField, NEW_CLUB_CORRECT_CONTACT_DETAILS.WHATSUP);
        await addclubpage.fillInputField(addclubpage.clubWebPageField, NEW_CLUB_CORRECT_CONTACT_DETAILS.webPage);
        
        await addclubpage.proceedToNextStep();

        await addclubpage.uploadLogo();
        await addclubpage.uploadCover();
        await addclubpage.uploadPhotoes();

        await addclubpage.fillInputField(addclubpage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);
        await addclubpage.completeClubCreation();

        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubCreatedSuccessMessage, true, successClubCreationMessage);
        
        await userpage.gotoUserPage();

        //verify that the created club information is correct
        clubinfopage = new ClubInfoPage(page);
        await userpage.gotoUserPage();
        await userpage.openClubInfoPage(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageTitle,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubTitle
        );
        await clubinfopage.expectElementToHaveText(clubinfopage.clubPageTags, [
            CLUB_CATEGORIES.acting,
            CLUB_CATEGORIES.developmentCenter,
            CLUB_CATEGORIES.programming,
        ]);
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageAddress,
            true,
            NEW_LOCATION_CORRECT_DETAILS.locationAddress
        );
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageDescription,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubDescription
        );
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.email)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.facebook)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, '+38'+NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.WHATSUP)
        await clubinfopage.expectElementToContainText(clubinfopage.clubPageContactData, NEW_CLUB_CORRECT_CONTACT_DETAILS.webPage)
        
        await clubinfopage.verifyElementVisibilityAndText(
            clubinfopage.clubPageDevelopmentCenter,
            true,
            NEW_CLUB_CORRECT_DETAILS.clubRelatedCenter
        );
    });
    

    test("Verify validation error messages appearence/disappearence on the first step", async ({ page }) => {
        await addclubpage.proceedToNextStep();
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, true, addClubValidationErrorMessages.NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.categoryFieldErrorMessage, true,addClubValidationErrorMessages.CATEGORY_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, true,addClubValidationErrorMessages.AGE_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageToFieldErrorMessage, true,addClubValidationErrorMessages.AGE_IS_MANDATORY);

        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_INCORRECT_DETAILS.clubTitle);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, true, addClubValidationErrorMessages.NAME_IS_INCORRECT);
        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.nameFieldErrorMessage, false);

        await addclubpage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.categoryFieldErrorMessage, false);

        await addclubpage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, false);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.ageFromFieldErrorMessage, false);
    });

    test("Verify validation error messages appearence/disappearence on the second step", async ({ page }) => {
        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
        );
        await addclubpage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addclubpage.proceedToNextStep();
        await addclubpage.openAddLocationWindow();

        //trigger validation of mandatory fields and verify that each is present and correct
        //message is displayed
        await addclubpage.confirmLocationAddition();
        
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, true, addLocationValidationErrorMessages.CITY_IS_MANDATORY);

        //this one is ommitted because of the defect which displays both mandatory and incorrect value validation for the address field
        //await addclubpage.verifyElementVisibilityAndText(addclubpage.locationAddressFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_MANDATORY);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_ARE_INCORRECT);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, true, addLocationValidationErrorMessages.PHONE_IS_MANDATORY);

        //fill fields with incorrect data and verify that appropriate error message appears
        await addclubpage.fillInputField(addclubpage.locationNameField, NEW_LOCATION_INCORRECT_DETAILS.locationName);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, true, addLocationValidationErrorMessages.LOCATION_NAME_IS_INCORRECT);

        await addclubpage.fillInputField(addclubpage.locationAddressField, NEW_LOCATION_INCORRECT_DETAILS.locationAddress);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationAddressFieldErrorMessage, true, addLocationValidationErrorMessages.ADDRESS_IS_INCORRECT);

        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, NEW_LOCATION_INCORRECT_DETAILS.locationCoordinates);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_ARE_INCORRECT);
        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, NEW_LOCATION_INCORRECT_DETAILS.locationCoordinatesWithLetters);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, true, addLocationValidationErrorMessages.COORDINATES_CANT_HAVE_LETTERS);

        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, NEW_LOCATION_INCORRECT_DETAILS.locationPhone);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, true, addLocationValidationErrorMessages.PHONE_IS_INCORRECT);

        //fill fields with correct data and verify that error messages disappear
        await addclubpage.fillInputField(addclubpage.locationNameField, NEW_LOCATION_CORRECT_DETAILS.locationName);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationNameFieldErrorMessage, false);

        await addclubpage.selectLocationCity(NEW_LOCATION_CORRECT_DETAILS.locationCity);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationAddressField, NEW_LOCATION_CORRECT_DETAILS.locationAddress);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCityErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationCoordinatesField, NEW_LOCATION_CORRECT_DETAILS.locationCoordinates);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationCoordinatesFieldErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.locationPhoneNumberField, NEW_LOCATION_CORRECT_DETAILS.locationPhone);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.locationPhoneFieldErrorMessage, false);

        await addclubpage.confirmLocationAddition();

        //trigger step 2 mandatory fields validation
        await addclubpage.proceedToNextStep();
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_MANDATORY);

        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.phoneNumberIncorrectFormat);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_INCORRECT);
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.phoneNumberIncorrectFormatWithLetters);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, true, addClubValidationErrorMessages.PHONE_IS_INCORRECT_CANT_HAVE_LETTERS);
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubPhoneFieldErrorMessage, false);

        await addclubpage.fillInputField(addclubpage.clubEmailField, NEW_CLUB_INCORRECT_CONTACT_DETAILS.email);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubEmailFieldErrorMessage, true, addClubValidationErrorMessages.EMAIL_IS_INCORRECT); 
        await addclubpage.fillInputField(addclubpage.clubEmailField, NEW_CLUB_CORRECT_CONTACT_DETAILS.email);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubEmailFieldErrorMessage, false); 



    });


    test("Verify validation error messages appearence/disappearence on the third step", async ({ page }) => {
        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.toggleStatedCheckboxes(
            CLUB_CATEGORIES.acting,
        );
        await addclubpage.setAcceptableChildAge(NEW_CLUB_CORRECT_DETAILS.clubEnterAge, NEW_CLUB_CORRECT_DETAILS.clubClosingAge);
        await addclubpage.proceedToNextStep();
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addclubpage.proceedToNextStep();

        await addclubpage.completeClubCreation();
        //to be updated once the correct error messages are implemented
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, true, addClubValidationErrorMessages.DESCRIPTION_IS_INCORRECT); 
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, NEW_CLUB_INCORRECT_DETAILS.clubDescription);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, true, addClubValidationErrorMessages.DESCRIPTION_IS_INCORRECT_CHAR_LIMIT); 
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);
        await addclubpage.verifyElementVisibilityAndText(addclubpage.clubDescriptionErrorMessage, false); 

    });


    test("Verify that tooltips and warning messages appear", async ({ page }) => {
        //remove the club with the test club_title if it exists
        try {
            await apiservice.deleteClubByTitle(NEW_CLUB_CORRECT_DETAILS.clubTitle);
        } catch (e) {
            console.error(e);
        }

        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addclubpage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addclubpage.proceedToNextStep();
        await addclubpage.openAddLocationWindow();

        await addclubpage.verifyLocationNameTooltipAppearsOnHover();
        await addclubpage.verifyLocationPhoneTooltipAppearsOnHover();
        await addclubpage.closeAddLocationWindow();

        await addclubpage.verifyAvailableOnlineTooltipAppearsOnHover();

        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addclubpage.proceedToNextStep();

        await addclubpage.verifyElementVisibility(addclubpage.clubIsAutomaticallyOnlineMessage, true);
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);

        //complete club creation and go through the creation steps again to verify that
        //'this club already exists' message appears
        await addclubpage.completeClubCreation();
        await homepage.openAddClubPage();
        await addclubpage.fillInputField(addclubpage.clubNameField, NEW_CLUB_CORRECT_DETAILS.clubTitle);
        await addclubpage.toggleStatedCheckboxes(CLUB_CATEGORIES.acting);
        await addclubpage.setAcceptableChildAge(
            NEW_CLUB_CORRECT_DETAILS.clubEnterAge,
            NEW_CLUB_CORRECT_DETAILS.clubClosingAge
        );
        await addclubpage.proceedToNextStep();
        await addclubpage.fillInputField(addclubpage.clubPhoneNumberField, NEW_CLUB_CORRECT_CONTACT_DETAILS.phoneNumber);
        await addclubpage.proceedToNextStep();
        await addclubpage.fillInputField(addclubpage.descriptionTextArea, NEW_CLUB_CORRECT_DETAILS.clubDescription);

        await addclubpage.completeClubCreation();
        await addclubpage.verifyElementVisibility(addclubpage.clubAlreadyExistMessage, true);
    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
