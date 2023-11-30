import { API_URL } from "../constants/api.constants";
import { USER_PAGE } from "../constants/locatorsText.constants";
import BasePage from "./BasePage";

class UserPage extends BasePage {
    constructor(page) {
        super(page);
        this.userPageTitle = page.locator(".menu-title");
        this.ownedClubs = page.locator("div.ant-card");
        this.clubsNames = page.locator("div.title-name");
        this.firstClub = page.locator("div.ant-space-item:first-child div.title-name");
        this.editClubOption = page.locator("li.ant-dropdown-menu-item").filter({ hasText: USER_PAGE.editClub });
        this.deleteClubOption = page.locator("li.ant-dropdown-menu-item").filter({ hasText: USER_PAGE.deleteClub });
        this.editClubWindowTitle = page.getByRole("dialog").getByText("Редагувати гурток");
        this.clubDetailsButton = page.locator("button.details-button");

        this.clubDeletedSuccessMessage = this.page
            .locator("div.ant-message-success")
            .filter({ hasText: USER_PAGE.clubSuccessfullyDeleted });
    }

    async gotoUserPage() {
        const idValue = await this.page.evaluate(() => {
            return window.localStorage.getItem("id");
        });
        await this.page.goto(`${API_URL}/user/${idValue}/page`);
    }

    async verifyTitleIsVisible(isVisible) {
        await this.verifyElementVisibility(this.userPageTitle, isVisible);
    }

    async openClubEditModeByTitle(clubTitle) {
        await this.selectClubDropdownOption(clubTitle, this.editClubOption);
        await this.verifyElementVisibility(this.editClubWindowTitle);
    }

    async removeClubByTitle(clubTitle) {
        await this.selectClubDropdownOption(clubTitle, this.deleteClubOption);
        await this.verifyElementVisibility(this.clubDeletedSuccessMessage);
    }

    async checkIfUserHasClubs() {
        try {
            await (await this.firstClub).waitFor({ state: "visible", timeout: 5000 });
        } catch (e) {
            throw new Error("This user doesn't own any clubs");
        }
    }

    /*
     *This method verifies whether there is a club with provided title on the user page
     *if the club is not present, it will iterate through the pages untill the required club is found and
     *then selecting the provided option from the dropdown
     *or if the are no pages left and club wasn't found, it will throw an error
     */
    async selectClubDropdownOption(clubTitle, option) {
        await this.checkIfUserHasClubs();

        const club = await this.ownedClubs.filter({ has: this.page.getByText(clubTitle, { exact: true }) });
        if (await club.isVisible()) {
            await club.locator("div.update-club-dropdown").click();
            await option.click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            await this.selectClubDropdownOption(clubTitle, option);
        } else {
            // If the club is not found and there are no more pages, throw an error
            throw new Error("No such club exists");
        }
    }

    async openClubInfoPage(clubTitle) {
        await this.checkIfUserHasClubs();

        const club = await this.ownedClubs.filter({ has: this.page.getByText(clubTitle, { exact: true }) });
        if (await club.isVisible()) {
            await club.locator(this.clubDetailsButton).click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            await this.openClubInfoPage(clubTitle);
        } else {
            // If the club is not found and there are no more pages, throw an error
            throw new Error("No such club exists");
        }
    }
}

module.exports = UserPage;
