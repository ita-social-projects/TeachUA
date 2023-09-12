import { expect} from "@playwright/test";
import {apiUrl} from "../constants/api.constants";
import BasePage from "../PO/basepage";

class UserPage extends BasePage{
    constructor(page) {
        super(page);
        this.userPageTitle = page.locator('.menu-title')
    }

    async gotoUserPage(){
        await this.page.reload();
        const idValue = await this.page.evaluate(() => {
            return window.localStorage.getItem('id');
        });
        await this.page.goto(`${apiUrl}/user/${idValue}/page`);
    }

    async verifyTitleVisible(isVisible){
        await this.elementToBeVisible(this.userPageTitle, isVisible);
    }
}

module.exports = UserPage;