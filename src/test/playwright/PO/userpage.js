import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import BasePage from "./basePage";

class UserPage extends BasePage{
    constructor(page) {
        super(page);
        this.userPageTitle = page.locator('.menu-title')
    }

    async gotoUserPage(){
        const idValue = await this.page.evaluate(() => {
            return window.localStorage.getItem('id');
        });
        await this.page.goto(`${apiUrl}/clubs`);
    }

    async verifyTitleIsVisible(isVisible){
        await this.elementToBeVisible(this.userPageTitle, isVisible);
    }
}

module.exports = UserPage;