import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import {clubOnlineSliderTooltip, successClubCreationMessage,addClubValidationErrorMessages} from "../constants/messages.constants";
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


        //Step 3 - Опис
        this.clubIsAutomaticallyOnlineMessage = this.page.locator('div.ant-message-notice-content');
        this.addLogoInput = this.page.locator('input#basic_urlLogo')
        this.addCoverInput = this.page.locator('input#basic_urlBackground')
        this.addImagesInput = this.page.locator('input[type="file"]').nth(2);
        this.descriptionTextArea = this.page.locator("textarea[placeholder='Додайте опис гуртка']");
        this.completeButton = this.page.getByRole("button", { name: "Завершити" });
        this.clubCreatedSuccessMessage = this.page.locator("div.ant-message-success");
        //Error messages

    }

    async gotoAddClubPage() {
        await this.addClubButton.click();
    }

    //Step #1
    async proceedToNextStep() {
        await this.nextStepButton.click();
    }

    async fillClubNameField(clubName) {
        await this.clubNameField.clear();
        await this.clubNameField.fill(clubName);
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

//Step 1: Error messages handling
    async verifyDisplayOfClubNameIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.nameFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.NAME_IS_MANDATORY)
    }
    async verifyDisplayOfClubNameIsIncorrectErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.nameFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.NAME_IS_INCORRECT);
    }

    async verifyDisplayOfClubCategoryIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.categoryFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.CATEGORY_IS_MANDATORY)
    }
    async verifyDisplayOfClubAgeFromIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.ageFromFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.AGE_IS_MANDATORY);
    }
    async verifyDisplayOfClubAgeToIsMandatoryErrorMessage(isDisplayed){
        await this.verifyElementVisibilityAndText(this.ageToFieldErrorMessage, isDisplayed, addClubValidationErrorMessages.AGE_IS_MANDATORY);
    }


    
//Step #2
//Methods related to the new location addition window------
    async openAddLocationWindow() {
        await this.addLocationButton.click();
    }

    async fillLocationName(name) {
        await this.locationNameField.clear();
        await this.locationNameField.fill(name);
    }

    async fillLocationAddress(address) {
        await this.locationAddressField.clear();
        await this.locationAddressField.fill(address);
    }

    async fillLocationCoordinates(coordinates) {
        await this.locationCoordinatesField.clear();
        await this.locationCoordinatesField.fill(coordinates);
    }

    async fillLocationPhoneNumber(number) {
        await this.locationPhoneNumberField.clear();
        await this.locationPhoneNumberField.fill(number);
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

    //Addition of the club contact details
    async fillClubPhoneNumberField(number){
        await this.clubPhoneNumberField.clear();
        await this.clubPhoneNumberField.fill(number);
    }

    async fillClubFacebookField(facebook){
        await this.clubFacebookField.clear();
        await this.clubFacebookField.fill(facebook);
    }

    async fillClubWhatsUpField(whatsUp){
        await this.clubWhatsUpField.clear();
        await this.clubWhatsUpField.fill(whatsUp);
    }

    async fillClubEmailField(email){
        await this.clubEmailField.clear();
        await this.clubEmailField.fill(email);
    }

    async fillClubSkypeField(skype){
        await this.clubSkypeField.clear();
        await this.clubSkypeField.fill(skype);
    }

    async fillClubWebPageField(webPage){
        await this.clubWebPageField.clear();
        await this.clubWebPageField.fill(webPage);
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