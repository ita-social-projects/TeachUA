import { ADD_CLUB_PAGE } from "../constants/locatorsText.constants";
import {
    CLUB_ONLINE_SLIDER_TOOLTIP,
    LOCATION_NAME_TOOLTIP,
    LOCATION_PHONE_TOOLTIP,
} from "../constants/messages.constants";
import { IMAGES_PATH } from "../constants/general.constants";
import BasePage from "./BasePage";

class AddClubPage extends BasePage {
    constructor(page) {
        super(page);
        //Step 1 - Основна Інформація
        this.goBackButton = this.page.getByRole("button", { name: ADD_CLUB_PAGE.stepBack });
        this.nextStepButton = this.page.getByRole("button", { name: ADD_CLUB_PAGE.nextStep });
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
        this.locationAddButton = this.page.locator('div.add-club-locations button[type="submit"]');

        this.locationNameInfoSign = this.page.locator('input#name + span svg');
        this.locationPhoneInfoSign = this.page.locator('input#phone + span svg');

        this.locationCloseButton = this.page.locator('button[aria-label="Close"]').nth(1);

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
        this.clubIsAutomaticallyOnlineMessage = this.page.locator('div.ant-message-notice-content').filter({ hasText: ADD_CLUB_PAGE.noLocationClubOnlineMessage });;
        this.addLogoInput = this.page.locator('input#basic_urlLogo')
        this.addCoverInput = this.page.locator('input#basic_urlBackground')
        this.addImagesInput = this.page.locator('div.ant-upload-list-picture-card input');
        this.descriptionTextArea = this.page.locator("textarea[placeholder='Додайте опис гуртка']");
        this.completeButton = this.page.getByRole("button", { name: "Завершити" });

        this.clubCreatedSuccessMessage = this.page.locator("div.ant-message-success");
        this.clubAlreadyExistMessage = this.page.locator("div.ant-message-notice-content").filter({ hasText: ADD_CLUB_PAGE.clubAlreadyExistMessage});;
        //Error messages
        this.clubDescriptionErrorMessage = this.page.locator("div#basic_description_help");

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
        await this.relatedCenterDropdown.click({force: true});
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
        await this.locationCityDropdown.click({force: true});
        await this.selectDropdownValue(value);
    }

    async selectLocationDistrict(value) {
        await this.locationDistrictDropdown.click({force: true});
        await this.selectDropdownValue(value);
    }

    async selectLocationStation(value) {
        await this.locationStationDropdown.click({force: true});
        await this.selectDropdownValue(value);
    }

    async confirmLocationAddition(){
        await this.locationAddButton.click();
    }

    async verifyLocationNameTooltipAppearsOnHover(){
        await this.verifyTooltipAppearsOnHover(this.locationNameInfoSign, LOCATION_NAME_TOOLTIP)
    }

    async verifyLocationPhoneTooltipAppearsOnHover(){
        await this.verifyTooltipAppearsOnHover(this.locationPhoneInfoSign, LOCATION_PHONE_TOOLTIP)
    }

    async closeAddLocationWindow(){
        await this.locationCloseButton.click();
    }

    //---------------------------------------------------------------------

    async verifyLocationPresence(locationName, isPresent){
        const location = await this.page.getByRole('heading', {name: locationName});
        await this.verifyElementVisibility(location, isPresent);
    }

    async toggleIsOnlineSlider(){
        await this.isOnlineSlider.click();
    }

    async verifyAvailableOnlineTooltipAppearsOnHover(){
        await this.verifyTooltipAppearsOnHover(this.isOnlineInfoSign, CLUB_ONLINE_SLIDER_TOOLTIP)
    }

    //Step #3

    async uploadLogo(){
        await this.addLogoInput.setInputFiles(IMAGES_PATH + 'clubLogo.jpg');
    }

    async uploadCover(){
        await this.addCoverInput.setInputFiles(IMAGES_PATH + 'cover.png');
    }

    async uploadPhotoes() {
        await this.addImagesInput.setInputFiles(IMAGES_PATH + 'galery.jpg');
    }

    async fillClubDescription(description){
        await this.descriptionTextArea.clear();
        await this.descriptionTextArea.fill(description);
    }

    async completeClubCreation(){
        await this.completeButton.click();
    }


}

module.exports = AddClubPage;