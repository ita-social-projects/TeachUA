import { editClubPage } from "../constants/locatorsText.constants";
import AddClubPage from "./AddClubPage";


class EditClubPage extends AddClubPage {
    constructor(page) {
        super(page);
        //Step 1 - Основна Інформація
        this.goBackButton = this.page.getByRole("button", { name: editClubPage.stepBack });
        this.nextStepButton = this.page.getByRole("button", { name: editClubPage.nextStep });
        this.clubNameField = this.page.locator("input#edit_category_name");
        this.childAgeFrom = this.page.locator("input#edit_category_ageFrom");
        this.childAgeTo = this.page.locator("input#edit_category_ageTo");
        this.relatedCenterDropdown = this.page.locator("input#edit_category_centerId");
        //Error messages
        this.nameFieldErrorMessage = this.page.locator("div#edit_category_name_help");
        this.categoryFieldErrorMessage = this.page.locator("div#edit_category_selectedCategories_help");
        this.ageFromFieldErrorMessage = this.page.locator("div#edit_category_ageFrom_help");
        this.ageToFieldErrorMessage = this.page.locator("div#edit_category_ageTo_help");

        //Step 2 - Контакти/Локація
        this.addLocationButton = this.page.locator("span.add-club-location");
        this.editLocationButton = this.page.locator("ul.ant-list-item-action li span:first-child");
        this.deleteLocationButton = this.page.locator("ul.ant-list-item-action li span:last-child");
        this.deleteLocationCancelButton = this.page.locator("div.ant-popover-content button.popConfirm-cancel-button");
        this.deleteLocationConfirmButton = this.page.locator("div.ant-popover-content button.popConfirm-ok-button");

        this.locationNameField = this.page.locator("input#name");
        this.locationCityDropdown = this.page.locator("input#cityName");
        this.locationDistrictDropdown = this.page.locator("input#districtName");
        this.locationStationDropdown = this.page.locator("input#stationName");
        this.locationAddressField = this.page.locator("input#address");
        this.locationCoordinatesField = this.page.locator("input#coordinates");
        this.locationPhoneNumberField = this.page.locator("input#phone");
        this.locationAddButton = this.page.locator('div.add-club-locations button[type="submit"]');
        this.locationCloseButton = this.page.locator('button[aria-label="Close"]').nth(1);

        this.locationNameInfoSign = this.page.locator("input#name + span svg");
        this.locationPhoneInfoSign = this.page.locator("input#phone + span svg");

        this.locationsList = this.page.locator("div.add-club-location-list");

        this.isOnlineSlider = this.page.locator('button[role="switch"]');
        this.isOnlineInfoSign = this.page.locator('button[role="switch"] + span svg');

        this.clubPhoneNumberField = this.page.locator("input#basic_Телефон");
        this.clubFacebookField = this.page.locator("input#basic_Facebook");
        this.clubWhatsUpField = this.page.locator("input#basic_WhatsApp");
        this.clubEmailField = this.page.locator("input#basic_Пошта");
        this.clubSkypeField = this.page.locator("input#basic_Skype");
        this.clubWebPageField = this.page.locator("input#basic_Site");
        //Error messages
        this.locationNameFieldErrorMessage = this.page.locator("div#name_help");
        this.locationCityErrorMessage = this.page.locator("div#cityName_help");
        this.locationAddressFieldErrorMessage = this.page.locator("div#address_help");
        this.locationCoordinatesFieldErrorMessage = this.page.locator("div#coordinates_help");
        this.locationPhoneFieldErrorMessage = this.page.locator("div#phone_help");

        this.clubPhoneFieldErrorMessage = this.page.locator("div#basic_Телефон_help");
        this.clubEmailFieldErrorMessage = this.page.locator("div#basic_Пошта_help");

        //Step 3 - Опис
        this.clubIsAutomaticallyOnlineMessage = this.page
            .locator("div.ant-message-notice-content")
            .filter({ hasText: editClubPage.noLocationClubOnlineMessage });
        this.addLogoInput = this.page.locator("input#basic_urlLogo");
        this.addCoverInput = this.page.locator("input#basic_urlBackground");
        this.addImagesInput = this.page.locator("div.ant-upload-list-picture-card input");
        this.descriptionTextArea = this.page.locator("textarea#basic_descriptionText");
        this.completeButton = this.page.getByRole("button", { name: "Завершити" });

        this.clubUpdatedSuccessMessage = this.page
            .locator("div.ant-message-success")
            .filter({ hasText: editClubPage.clubSuccessfullyEdited });
            
        this.clubAlreadyExistMessage = this.page
            .locator("div.ant-message-notice-content")
            .filter({ hasText: editClubPage.clubAlreadyExistMessage });
        //Error messages
        this.clubDescriptionErrorMessage = this.page.locator("div#basic_descriptionText_help");
    }

    async openEditLocationWindow(locationName) {
        await this.page
            .locator("li.ant-list-item")
            .filter({ hasText: locationName })
            .locator(this.editLocationButton)
            .click();
    
        }
    async deleteLocation(locationName) {
        await this.page
            .locator("li.ant-list-item")
            .filter({ hasText: locationName })
            .locator(this.deleteLocationButton)
            .click();
        await this.deleteLocationConfirmButton.click();
    }
}

module.exports = EditClubPage;