import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import {clubOnlineSliderTooltip, successClubCreationMessage,addClubValidationErrorMessages, addLocationValidationErrorMessages} from "../constants/messages.constants";
import {imagesPath} from "../constants/general.constants";
import BasePage from "./BasePage";

class AddClubPage extends BasePage {
    constructor(page) {
        super(page);
        //Step 1 - Основна Інформація
        this.addClubButton = this.page.getByRole("button", { name: "Додати гурток" });
        this.goBackButton = this.page.getByRole("button", { name: "Назад" });
        this.nextStepButton = this.page.getByRole("button", { name: "Наступний крок" });
        this.clubNameField = this.page.locator("input#basic_name");
        this.childAgeFrom = this.page.locator("input#basic_ageFrom");
        this.childAgeTo = this.page.locator("input#basic_ageTo");
        this.relatedCenterDropdown = this.page.locator("input#basic_centerId");
        //Error messages
        this.nameFieldErrorMessage = this.page.locator("div#basic_name_help");
        this.categoryFieldErrorMessage = this.page.locator("div#basic_categories_help");
        this.ageFromFieldErrorMessage = this.page.locator("div#basic_ageFrom_help");
        this.ageToFieldErrorMessage = this.page.locator("div#basic_ageTo_help");

        //Step 2 - Контакти/Локація
        this.addLocationButton = this.page.locator("span.add-club-location");
        this.locationNameField = this.page.locator("input#name");
        this.locationCityDropdown = this.page.locator("input#cityName");
        this.locationDistrictDropdown = this.page.locator("input#districtName");
        this.locationStationDropdown = this.page.locator("input#stationName");
        this.locationAddressField = this.page.locator("input#address");
        this.locationCoordinatesField = this.page.locator("input#coordinates");
        this.locationPhoneNumberField = this.page.locator("input#phone");
        this.locationAddButton = this.page.locator('button[type="submit"]').filter({ hasText: "Додати" });

        this.locationsList = this.page.locator('div.add-club-location-list ul.ant-list-items');

        this.isOnlineSlider = this.page.locator('button[role="switch"]');
        this.isOnlineInfoSign = this.page.locator('button[role="switch"] + span svg');

        this.clubPhoneNumberField = this.page.locator('input#basic_contactТелефон')
        this.clubFacebookField = this.page.locator('input#basic_contactFacebook')
        this.clubWhatsUpField = this.page.locator('input#basic_contactWhatsApp')
        this.clubEmailField = this.page.locator('input#basic_contactПошта')
        this.clubSkypeField = this.page.locator('input#basic_contactSkype')
        this.clubWebPageField = this.page.locator('input#basic_contactSite')
        //Error messages
        this.locationNameFieldErrorMessage = this.page.locator("div#name_help");
        this.locationCityErrorMessage = this.page.locator("div#cityName_help");
        this.locationAddressFieldErrorMessage = this.page.locator("div#address_help");
        this.locationCoordinatesFieldErrorMessage = this.page.locator("div#coordinates_help");
        this.locationPhoneFieldErrorMessage = this.page.locator("div#phone_help");

        this.clubPhoneFieldErrorMessage = this.page.locator("div#basic_contactТелефон_help");
        this.clubEmailFieldErrorMessage = this.page.locator("div#basic_contactПошта_help");

        //Step 3 - Опис
        this.clubIsAutomaticallyOnlineMessage = this.page.locator('div.ant-message-notice-content');
        this.addLogoInput = this.page.locator('input#basic_urlLogo')
        this.addCoverInput = this.page.locator('input#basic_urlBackground')
        this.addImagesInput = this.page.locator('input[type="file"]').nth(2);
        this.descriptionTextArea = this.page.locator("textarea[placeholder='Додайте опис гуртка']");
        this.completeButton = this.page.getByRole("button", { name: "Завершити" });
        this.clubCreatedSuccessMessage = this.page.locator("div.ant-message-success");
        //Error messages
        this.clubDescriptionErrorMessage = this.page.locator("div#basic_description_help");

    }

    async gotoAddClubPage() {
        await this.addClubButton.click();
    }

    //Step #1
    async proceedToNextStep() {
        await this.nextStepButton.click();
    }

    async setAcceptableChildAge(from, to) {
        if (isNaN(from) || isNaN(to)) {
            throw new Error("Only numbers are allowed!");
        } else {
            await this.childAgeFrom.fill(from);
            await this.childAgeTo.fill(to);
        }
    }

    async toggleStatedCheckboxes(...labels) {
        for (let label of labels) {
            const checkbox = await this.page.locator(`input[type="checkbox"][value="${label}"]`);
            if (await checkbox.isVisible()) {
                await checkbox.check();
            } else {
                throw new Error(`Checkbox with value ${label} is not present on the page`);
            }
        }
    }

    async selectRelatedCenter(center) {
        await this.relatedCenterDropdown.click();
        await this.selectDropdownValue(center);
    }

    async selectDropdownValue(value) {
        await this.page
            .locator("div.ant-select-item-option-content")
            .filter({ hasText: `${value}` })
            .click();
    }

    
//Step #2
//Methods related to the new location addition window------
    async openAddLocationWindow() {
        await this.addLocationButton.click();
    }

    async selectLocationCity(value) {
        await this.locationCityDropdown.click();
        await this.selectDropdownValue(value);
    }

    async selectLocationDistrict(value) {
        await this.locationDistrictDropdown.click();
        await this.selectDropdownValue(value);
    }

    async selectLocationStation(value) {
        await this.locationStationDropdown.click();
        await this.selectDropdownValue(value);
    }

    async confirmLocationAddition(){
        await this.locationAddButton.click();
    }

    //Location addition window: Error messages handling

    //to be updated on a valid error message (currently only one error message for both mandatory validation and incorrect input)
    //Name field
    async verifyDisplayOfLocationNameIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationNameFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.LOCATION_NAME_IS_MANDATORY)
    }

    //City dropdown
    async verifyDisplayOfLocationCityIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationCityErrorMessage, isDisplayed, addLocationValidationErrorMessages.CITY_IS_MANDATORY)
    }

    //Address Field
    async verifyDisplayOfLocationAddressIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationAddressFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.ADDRESS_IS_MANDATORY)
    }
    async verifyDisplayOfLocationAddressIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationAddressFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.ADDRESS_IS_INCORRECT)
    }


    //Coordinates Field
    async verifyDisplayOfLocationCoordinatesAreIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationCoordinatesFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.COORDINATES_ARE_INCORRECT)
    }
    async verifyDisplayOfLocationCoordinatesCantHaveLettersErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationCoordinatesFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.
            COORDINATES_CANT_HAVE_LETTERS)
    }

    //Phone Field
    async verifyDisplayOfLocationPhoneIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationPhoneFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.PHONE_IS_MANDATORY)
    }
    async verifyDisplayOfLocationPhoneIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.locationPhoneFieldErrorMessage, isDisplayed, addLocationValidationErrorMessages.PHONE_IS_INCORRECT)
    }


    //---------------------------------------------------------------------

    async verifyLocationListContainsItem(item){
        await this.expectElementToContainText(this.locationsList, item);
    }

    async toggleIsOnlineSlider(){
        await this.isOnlineSlider.click();
    }

    async verifyAvailableOnlineTooltipAppearsOnHover(){
        await this.verifyTooltipAppearsOnHover(this.isOnlineInfoSign, clubOnlineSliderTooltip)
    }

    //Contact details - error messages
    async verifyDisplayOfClubPhoneIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.clubPhoneFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.PHONE_IS_MANDATORY)
    }

    async verifyDisplayOfClubPhoneIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.clubPhoneFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.PHONE_IS_INCORRECT)
    }
    async verifyDisplayOfClubPhoneCantHaveLettersErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.clubPhoneFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.PHONE_IS_INCORRECT_CANT_HAVE_LETTERS)
    }
    
    async verifyDisplayOfClubEmailIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.clubEmailFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.EMAIL_IS_INCORRECT)
    }

    //Step #3

    async uploadLogo(){
        await this.addLogoInput.setInputFiles(imagesPath + 'clubLogo.jpg');
    }

    async uploadCover(){
        await this.addCoverInput.setInputFiles(imagesPath + 'cover.png');
    }

    async uploadPhotoes() {
        await this.addImagesInput.setInputFiles(imagesPath + 'galery.jpg');
    }

    async fillClubDescription(description){
        await this.descriptionTextArea.clear();
        await this.descriptionTextArea.fill(description);
    }

    async completeClubCreation(){
        await this.completeButton.click();
    }

    async verifyClubCreationSuccessMessageDisplayed(){
        await this.verifyElementVisibility(this.clubCreatedSuccessMessage, true);
        await this.expectElementToHaveText(this.clubCreatedSuccessMessage, successClubCreationMessage);
    }

}

module.exports = AddClubPage;