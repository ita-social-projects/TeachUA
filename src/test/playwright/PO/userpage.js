import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import {userPage} from "../constants/locatorsText.constants";
import BasePage from "./BasePage";

class UserPage extends BasePage {
    constructor(page) {
        super(page);
        this.userPageTitle = page.locator(".menu-title");
        this.ownedClubs = page.locator("div.ant-card");
        this.clubsNames = page.locator("div.title-name");
        this.firstClub = page.locator("div.ant-space-item:first-child div.title-name");
        this.editClubOption = page.locator("li.ant-dropdown-menu-item").filter({ hasText: userPage.editClub })
        this.deleteClubOption = page.locator("li.ant-dropdown-menu-item").filter({ hasText: userPage.deleteClub })
        this.editClubWindowTitle = page.getByRole('dialog').getByText('Редагувати гурток');

        this.clubDeletedSuccessMessage = this.page.locator("div.ant-message-success").filter({ hasText: userPage.clubSuccessfullyDeleted});
    }

    async gotoUserPage() {
        const idValue = await this.page.evaluate(() => {
            return window.localStorage.getItem("id");
        });
        await this.page.goto(`${apiUrl}/user/${idValue}/page`);
    }

    async verifyTitleIsVisible(isVisible) {
        await this.verifyElementVisibility(this.userPageTitle, isVisible);
    }

    async isClubPresent(clubTitle) {
        try{
        await (await this.firstClub).waitFor({ state: "visible", timeout: 5000 });
        } catch(e){
            console.log("Current user doesn't own any clubs");
            return false;
        }
        const clubNames = await this.clubsNames.allTextContents();
        if (await clubNames.includes(clubTitle)) {
            return true;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            return await this.isClubPresent(clubTitle);
        } else {
            // If the club is not found and there are no more pages, return false
            return false;
        }
    }

    async verifyClubExistance(clubTitle){
        expect(await this.isClubPresent(clubTitle)).toBe(true);
    }

    async openClubEditModeByTitle(clubTitle){
        await this.selectClubDropdownOption(clubTitle, this.editClubOption);
        await this.verifyElementVisibility(this.editClubWindowTitle);
    }

    async removeClubByTitle(clubTitle){
        await this.selectClubDropdownOption(clubTitle, this.deleteClubOption)
        await this.verifyElementVisibility(this.clubDeletedSuccessMessage);
    }

    /*
    *This method verifies whether there is a club with provided title on the user page
    *if the club is not present, it will iterate through the pages untill the required club is found and
    *then selecting the provided option from the dropdown
    *or if the are no pages left and club wasn't found, it will throw an error
    */
    async selectClubDropdownOption(clubTitle, option){
        try{
        await (await this.firstClub).waitFor({ state: "visible", timeout: 5000 });
        } catch(e){
            throw new Error("This user doesn't own any clubs")
        }

        const club = await this.ownedClubs.filter({ has: this.page.getByText(clubTitle, {exact: true}) });
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
}

module.exports = UserPage;