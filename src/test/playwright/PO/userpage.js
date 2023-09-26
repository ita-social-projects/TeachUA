import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import BasePage from "./BasePage";

class UserPage extends BasePage{
    constructor(page) {
        super(page);
        this.userPageTitle = page.locator('.menu-title');
        this.ownedClubs = page.locator('div.ant-card')
    }

    async gotoUserPage(){
        const idValue = await this.page.evaluate(() => {
            return window.localStorage.getItem('id');
        });
        await this.page.goto(`${apiUrl}/user/${idValue}/page`);
    }

    async verifyTitleIsVisible(isVisible){
        await this.verifyElementVisibility(this.userPageTitle, isVisible);
    }

    async verifyClubExistsByTitle(clubTitle){
        const club = await this.ownedClubs.filter({hasText: clubTitle});
        await this.verifyElementVisibility(club,true);
    }

    async removeClubByTitle(clubTitle){
        const club = await this.ownedClubs.filter({hasText: clubTitle});
        await club.locator('div.update-club-dropdown').click();
        await this.page.locator('li.ant-dropdown-menu-item')
        .filter({hasText: "Видалити гурток"})
        .click();
    }
}

module.exports = UserPage;